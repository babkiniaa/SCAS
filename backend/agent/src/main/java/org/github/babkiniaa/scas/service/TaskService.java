package org.github.babkiniaa.scas.service;

import lombok.RequiredArgsConstructor;
import net.sourceforge.pmd.reporting.RuleViolation;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.github.babkiniaa.scas.client.AgentClient;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.dto.Request.StartAnalyseDto;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.github.babkiniaa.scas.dto.Response.ReportAndDirDto;
import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.github.babkiniaa.scas.dto.typeForMap.MethodAndTypeAnalysis;
import org.github.babkiniaa.scas.entity.StatusTask;
import org.github.babkiniaa.scas.entity.Task;
import org.github.babkiniaa.scas.mapper.ReportMapper;
import org.github.babkiniaa.scas.mapper.ReportOWASPMapper;
import org.github.babkiniaa.scas.mapper.ReportPMDMapper;
import org.github.babkiniaa.scas.mapper.TaskMapper;
import org.github.babkiniaa.scas.repository.TaskRepository;
import org.github.babkiniaa.scas.utils.DeleteFileUtil;
import org.github.babkiniaa.scas.utils.GitUtil;
import org.github.babkiniaa.scas.utils.analysis.StaticAnalysis;
import org.owasp.dependencycheck.dependency.Dependency;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The type Task service.
 */
@Service
@RequiredArgsConstructor
public class TaskService {

    private final AgentClient agentClient;
    private final TaskMapper taskMapper;
    private final ReportPMDMapper reportPMDMapper;
    private final ReportOWASPMapper reportOWASPMapper;
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
        Task task = taskMapper.RegisterTaskToTask(registerTaskDto);
        task.setStatusTask(StatusTask.TODO);
        Long idTask = taskRepository.save(task).getId();
        StartAnalyseDto startAnalyseDto = new StartAnalyseDto(idTask, registerTaskDto.getIdProject(), registerTaskDto.getUrl(), registerTaskDto.getNeedReports());

        threadPoolExecutor.execute(() -> {
            try {
                startAnalysis(startAnalyseDto);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return idTask;
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
        return reportAndDir;
    }

    private ReportAndDirDto reportCheckstyle(ReportAndDirDto reportAndDir) {
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
    public long startAnalysis(StartAnalyseDto startAnalyseDto) {
        long taskId;
        Task task = taskRepository.findById(startAnalyseDto.getTaskId()).get();
        task.setStatusTask(StatusTask.Run);
        taskRepository.save(task);
        String dir = System.getProperty("user.dir") + "/down/" + startAnalyseDto.getTaskId();
        ReportAndDirDto reportAndDirDto = new ReportAndDirDto();
        reportAndDirDto.setDir(dir);

        try {
            GitUtil.cloneRepository(startAnalyseDto.getUrl(), dir);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        try {
            for (String run : startAnalyseDto.getNeedReports()) {
                reportAndDirDto = (ReportAndDirDto) methodMap.get(run).getFunction().apply(reportAndDirDto);
            }
            task.setStatusTask(StatusTask.EndS);
            task.setReport(reportService.save(reportMapper.ReportAndDirDtoToReportDto(reportAndDirDto)));
            taskId = taskRepository.save(task).getId();
            saveReportInMaster(task, reportMapper.ReportAndDirDtoToReportDto(reportAndDirDto), startAnalyseDto.getProjectId());
        } catch (Exception e) {
            task.setStatusTask(StatusTask.Err);
            taskRepository.save(task).getId();
            throw new RuntimeException(e);
        } finally {
            DeleteFileUtil.deleteDir(new File(dir));
        }

        return taskId;
    }

    /**
     * Gets status by project id.
     *
     * @param projectId the project id
     * @return the status by project id
     */
    public StatusTask getStatusByProjectId(long projectId) {
        return taskRepository.findTaskByIdProject(projectId).get().getStatusTask();
    }

    /**
     * Save report in master.
     *
     * @param task      the task
     * @param reportDto the report dto
     * @param projectId the project id
     */
    /*
     * Тут надо бы еще хеш посчитать 🙄
     */
    public void saveReportInMaster(Task task, ReportDto reportDto, long projectId) {
        if (getStatusByProjectId(projectId) == StatusTask.EndS) {
            reportDto.setHash("You method hash");
            agentClient.saveInMasterReport(projectId, reportDto);
            taskRepository.delete(task);
        }
    }


}
