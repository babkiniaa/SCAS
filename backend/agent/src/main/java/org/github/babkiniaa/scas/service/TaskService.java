package org.github.babkiniaa.scas.service;

import lombok.RequiredArgsConstructor;
import net.sourceforge.pmd.reporting.RuleViolation;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.github.babkiniaa.scas.client.AgentClient;
import org.github.babkiniaa.scas.dto.GitDto;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.dto.Request.StartAnalyseDto;
import org.github.babkiniaa.scas.dto.Response.ReportAndIdProjectDto;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.github.babkiniaa.scas.dto.Response.ReportAndDirDto;
import org.github.babkiniaa.scas.dto.Response.TaskInQueueDto;
import org.github.babkiniaa.scas.dto.reportsDto.BugInstanceCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.DeadCodeDto.SummaryCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.ViolationCustomDto;
import org.github.babkiniaa.scas.dto.typeForMap.MethodAndTypeAnalysis;
import org.github.babkiniaa.scas.entity.StatusTask;
import org.github.babkiniaa.scas.entity.Task;
import org.github.babkiniaa.scas.entity.reportsEntity.DeadCodeEntity.SummaryCustom;
import org.github.babkiniaa.scas.mapper.*;
import org.github.babkiniaa.scas.mapper.deadCode.DeadReportMapper;
import org.github.babkiniaa.scas.mapper.deadCode.SummariesMapper;
import org.github.babkiniaa.scas.repository.TaskRepository;
import org.github.babkiniaa.scas.utils.DeleteFileUtil;
import org.github.babkiniaa.scas.utils.GitUtil;
import org.github.babkiniaa.scas.utils.analysis.BinAnalysis;
import org.github.babkiniaa.scas.utils.analysis.StaticAnalysis;
import org.owasp.dependencycheck.dependency.Dependency;
import org.shchek.exps.Summary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The type Task service.
 */
@Service
@RequiredArgsConstructor
public class TaskService {

    private final KafkaTemplate<String, ReportAndIdProjectDto> kafkaTemplate;
    private final KafkaTemplate<String, Double> kafkaTemplateLoad;
    private final AgentClient agentClient;
    private final TaskMapper taskMapper;
    private final ReportPMDMapper reportPMDMapper;
    private final ReportOWASPMapper reportOWASPMapper;
    private final SummariesMapper summariesMapper;
    private final ReportMapper reportMapper;
    private final ReportService reportService;
    private final TaskRepository taskRepository;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final HashMap<String, MethodAndTypeAnalysis> methodMap = new HashMap<>();

    {
        methodMap.put("PMD", new MethodAndTypeAnalysis(this::reportPmd, "Static"));
        methodMap.put("CheckStyle", new MethodAndTypeAnalysis(this::reportCheckstyle, "Static"));
        methodMap.put("SpotBugs", new MethodAndTypeAnalysis(this::reportSpotBugs, "Binary"));
        methodMap.put("OWASP", new MethodAndTypeAnalysis(this::reportOwasp, "Binary"));
        methodMap.put("DieDead", new MethodAndTypeAnalysis(this::reportDieDead, "Binary"));
    }

    /**
     * Gets method map.
     *
     * @return the method map
     */
    public HashMap<String, List<String>> getMethodMap() {
        HashMap<String, List<String>> hashMap = new HashMap<>();

        for (String m : methodMap.keySet()) {
            hashMap.computeIfAbsent(methodMap.get(m).getType().toLowerCase(), k -> new ArrayList<>()).add(m);
        }

        return hashMap;
    }

    /**
     * Save task long.
     *
     * @param registerTaskDto the register task dto
     * @return the long
     */
    public Long saveTask(RegisterTaskDto registerTaskDto) {
        Task task = taskMapper.registerTaskToTask(registerTaskDto);
        task.setStatusTask(StatusTask.TODO);
        Long idTask = taskRepository.save(task).getId();
        StartAnalyseDto startAnalyseDto = taskMapper.registerTaskToStartAnalyze(registerTaskDto);
        startAnalyseDto.setTaskId(idTask);
        threadPoolExecutor.execute(() -> {
            try {
                startAnalysis(startAnalyseDto);
            } catch (Exception e) {
                task.setStatusTask(StatusTask.Err);
                task.setMessage(e.getMessage());
                taskRepository.save(task);
                throw new RuntimeException(e);
            }
        });

        return idTask;
    }

    @Scheduled(fixedRate = 1000)
    public void agentLoad() {
        int totalThreads = threadPoolExecutor.getMaximumPoolSize();
        int busyThreads = threadPoolExecutor.getActiveCount();
        double loadFactor = (double) busyThreads / totalThreads;

        kafkaTemplateLoad.send("task-load-events-topic", null, loadFactor);
    }

    private ReportAndDirDto reportDieDead(ReportAndDirDto reportAndDir) {
        List<SummaryCustomDto> summaryDtos;
        List<Summary> summaries = new ArrayList<>();
        String dir = reportAndDir.getDir();
        summaries = BinAnalysis.DieDead(dir);

        summaryDtos = summariesMapper.sumstoDTO(summaries);

        reportAndDir.setSummaryCustoms(summaryDtos);

        return reportAndDir;
    }

    private ReportAndDirDto reportOwasp(ReportAndDirDto reportAndDir) {
        List<DependencyCustomDto> dependencyCustomDtos;
        List<Dependency> dependencies = new ArrayList<>();
        String dir = reportAndDir.getDir();
        dependencies = StaticAnalysis.startOWASP(dir);

        dependencyCustomDtos = reportOWASPMapper.owaspToOwaspCustomList(dependencies);

        reportAndDir.setDependencyCustoms(dependencyCustomDtos);

        return reportAndDir;
    }

    private ReportAndDirDto reportSpotBugs(ReportAndDirDto reportAndDir) {
        List<BugInstanceCustomDto> bugInstanceCustomDtos;
        try {
            bugInstanceCustomDtos = BinAnalysis.spotbugs(reportAndDir.getDir());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        reportAndDir.setBugInstanceCustoms(bugInstanceCustomDtos);
        return reportAndDir;
    }

    private ReportAndDirDto reportCheckstyle(ReportAndDirDto reportAndDir) {
        List<ViolationCustomDto> violationCustomDtos;

        try {
            violationCustomDtos = StaticAnalysis.startCheckStyle(reportAndDir.getDir());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        reportAndDir.setViolationCustoms(violationCustomDtos);

        return reportAndDir;
    }

    private ReportAndDirDto reportPmd(ReportAndDirDto reportAndDir) {

        List<RuleViolationCustomDto> violationCustomDtos = new ArrayList<>();
        List<RuleViolation> ruleViolationList = new ArrayList<>();
        String dir = reportAndDir.getDir();

        try {
            ruleViolationList = StaticAnalysis.startPmd(dir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        violationCustomDtos = reportPMDMapper.PMDtoDTO(ruleViolationList);
        reportAndDir.setRuleViolationCustoms(violationCustomDtos);

        return reportAndDir;
    }

    /**
     * Start analysis long.
     *
     * @param startAnalyseDto the start analyse dto
     * @return the long
     */
    @Async
    public void startAnalysis(StartAnalyseDto startAnalyseDto) {
        Task task = taskRepository.findById(startAnalyseDto.getTaskId()).get();
        System.out.println(task.getStatusTask());
        if (task.getStatusTask().equals(StatusTask.TODO)) {
            task.setStatusTask(StatusTask.Run);
            taskRepository.save(task);
            String dir = System.getProperty("user.dir") + "/down/" + startAnalyseDto.getTaskId();
            ReportAndDirDto reportAndDirDto = new ReportAndDirDto();
            reportAndDirDto.setDir(dir);
            GitDto gitDto = new GitDto();
            try {
                gitDto = GitUtil.cloneRepository(startAnalyseDto.getUrl(), dir, startAnalyseDto.getBranch(), startAnalyseDto.getCommit());
                reportAndDirDto.setHash(gitDto.getHash());
                reportAndDirDto.setBranch(gitDto.getBranch());
            } catch (GitAPIException | IOException e) {
                task.setStatusTask(StatusTask.Err);
                task.setMessage(e.getMessage());
                taskRepository.save(task);
                throw new RuntimeException(e);
            }
            try {
                List<String> lastAnalyze = agentClient.getAnalyzers(reportAndDirDto.getHash(), startAnalyseDto.getIdProject());
                if(!lastAnalyze.isEmpty()) {
                    List<String> filterAnalyze = startAnalyseDto.getNeedReports();
                    for (String analyze : lastAnalyze) {
                        filterAnalyze.remove(analyze);
                    }
                    startAnalyseDto.setNeedReports(filterAnalyze);
                }
                for (String run : startAnalyseDto.getNeedReports()) {
                    reportAndDirDto = (ReportAndDirDto) methodMap.get(run).getFunction().apply(reportAndDirDto);
                }
                task.setStatusTask(StatusTask.EndS);
                task.setReport(reportService.save(reportMapper.ReportAndDirDtoToReportDto(reportAndDirDto)));
                taskRepository.save(task);
                saveReportInMaster(task, reportMapper.ReportAndDirDtoToReportDto(reportAndDirDto), startAnalyseDto.getIdProject(), startAnalyseDto.getNeedReports());
            } catch (Exception e) {
                task.setStatusTask(StatusTask.Err);
                task.setMessage(e.getMessage());
                taskRepository.save(task);
                throw new RuntimeException(e);
            } finally {
                DeleteFileUtil.deleteDir(new File(dir));
            }
        }
    }

    /**
     * Gets status by project id.
     *
     * @param projectId the project id
     * @return the status by project id
     */
    public Task getStatusByProjectId(long projectId) {
        return taskRepository.findTaskByIdProject(projectId).get();
    }

    /**
     * Save report in master.
     *
     * @param task      the task
     * @param reportDto the report dto
     * @param projectId the project id
     */
    @Async
    public void saveReportInMaster(Task task, ReportDto reportDto, long projectId, List<String> needReports) {
        if (getStatusByProjectId(projectId).getStatusTask() == StatusTask.EndS) {
            reportDto.setAnalyzers(needReports);
            ReportAndIdProjectDto reportAndIdProjectDto = reportMapper.ReportDtoToReportAndIdProjectDto(reportDto);
            reportAndIdProjectDto.setProjectId(projectId);
            CompletableFuture<SendResult<String, ReportAndIdProjectDto>> future =
                    kafkaTemplate.send("report-create-events-topic", null, reportAndIdProjectDto);

            taskRepository.delete(task);
        }
    }

    /**
     * Ban task.
     *
     * @param taskId the task id
     */
    @Async
    public void banTask(long taskId) {
        try {
            Task task = taskRepository.findById(taskId).get();
            task.setStatusTask(StatusTask.Ban);
            taskRepository.save(task);
        } catch (Exception e) {

        }
    }

    /**
     * Gets run task.
     *
     * @return the run task
     */
    @Async
    public List<TaskInQueueDto> getRunTask() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskInQueueDto> taskInQueueDtos = new ArrayList<>();

        for (var task : tasks) {
            taskInQueueDtos.add(taskMapper.taskToTaskQueue(task));
        }

        return taskInQueueDtos;

    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public int getCount() {
        return threadPoolExecutor.getActiveCount();
    }


}
