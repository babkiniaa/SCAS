package org.github.babkiniaa.scas.service;

import lombok.RequiredArgsConstructor;
import net.sourceforge.pmd.reporting.RuleViolation;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.dto.Request.StartAnalyseDto;
import org.github.babkiniaa.scas.dto.Response.Report;
import org.github.babkiniaa.scas.dto.Response.ReportAndDir;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.github.babkiniaa.scas.dto.typeForMap.MethodAndTypeAnalysis;
import org.github.babkiniaa.scas.entity.StatusTask;
import org.github.babkiniaa.scas.entity.Task;
import org.github.babkiniaa.scas.mapper.ReportMapper;
import org.github.babkiniaa.scas.mapper.ReportPMDMapper;
import org.github.babkiniaa.scas.mapper.TaskMapper;
import org.github.babkiniaa.scas.repository.TaskRepository;
import org.github.babkiniaa.scas.utils.DeleteFileUtil;
import org.github.babkiniaa.scas.utils.GitUtil;
import org.github.babkiniaa.scas.utils.analysis.StaticAnalysis;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final ReportPMDMapper reportPMDMapper;
    private final ReportMapper reportMapper;
    private final TaskRepository taskRepository;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final HashMap<String, MethodAndTypeAnalysis> methodMap = new HashMap<>();

    {
        methodMap.put("PMD", new MethodAndTypeAnalysis(this::reportPmd, "Static"));
        methodMap.put("CheckStyle", new MethodAndTypeAnalysis(this::reportCheckstyle, "Static"));
        methodMap.put("SpotBugs", new MethodAndTypeAnalysis(this::reportSpotBugs, "Binary"));
        methodMap.put("OWASP", new MethodAndTypeAnalysis(this::reportOwasp, "Binary"));
    }

    public Long saveTask(RegisterTaskDto registerTaskDto) {
        Task task = taskMapper.RegisterTaskToTask(registerTaskDto);
        task.setStatusTask(StatusTask.TODO);
        Long idTask = taskRepository.save(task).getId();
        StartAnalyseDto startAnalyseDto = new StartAnalyseDto(idTask, registerTaskDto.getUrl(), registerTaskDto.getAnalysis());
        threadPoolExecutor.execute(() -> {
            try {
                startAnalysis(startAnalyseDto);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return idTask;
    }

    private ReportAndDir reportOwasp(ReportAndDir reportAndDir) {
        return reportAndDir;
    }

    private ReportAndDir reportSpotBugs(ReportAndDir reportAndDir) {
        return reportAndDir;
    }

    private ReportAndDir reportCheckstyle(ReportAndDir reportAndDir) {
        return reportAndDir;
    }

    private ReportAndDir reportPmd(ReportAndDir reportAndDir) {

        List<RuleViolationCustomDto> violationCustomDtos = new ArrayList<>();
        List<RuleViolation> ruleViolationList = new ArrayList<>();
        String dir = System.getProperty("user.dir") + "/down";

        try {
            ruleViolationList = StaticAnalysis.startPmd(dir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        violationCustomDtos = reportPMDMapper.PMDtoDTO(ruleViolationList);
        reportAndDir.setRuleViolationCustoms(violationCustomDtos);

        return reportAndDir;
    }

    @Async
    public Report startAnalysis(StartAnalyseDto startAnalyseDto) {
        Task task = taskRepository.findById(startAnalyseDto.getIdTask()).get();
        task.setStatusTask(StatusTask.Run);
        taskRepository.save(task);
        String dir = System.getProperty("user.dir") + "/down/" + startAnalyseDto.getIdTask();
        Report report = new Report();
        try {
            GitUtil.cloneRepository(startAnalyseDto.getUrl(), dir);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        for (String run : startAnalyseDto.getAnalysis()) {
            ReportAndDir reportAndDir = reportMapper.reportAndDirToReport(report);
            report = (Report) methodMap.get(run).getFunction().apply(reportAndDir);
        }
        DeleteFileUtil.deleteDir(new File(dir));
        task.setStatusTask(StatusTask.EndF);
        taskRepository.save(task);

        return report;
    }

}
