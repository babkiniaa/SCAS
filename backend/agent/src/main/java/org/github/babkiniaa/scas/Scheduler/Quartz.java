package org.github.babkiniaa.scas.Scheduler;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.dto.Response.Report;
import org.github.babkiniaa.scas.dto.typeForMap.MethodAndTypeAnalysis;
import org.github.babkiniaa.scas.service.TaskService;
import org.github.babkiniaa.scas.utils.DeleteFileUtil;
import org.github.babkiniaa.scas.utils.GitUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;
import java.util.HashMap;

@RequiredArgsConstructor
public class Quartz implements Job {

    private final TaskService taskService;
    private final RegisterTaskDto registerTaskDto;
    private Report report;
    private final HashMap<String, MethodAndTypeAnalysis> methodMap = new HashMap<>();

    {
        methodMap.put("PMD", new MethodAndTypeAnalysis(this::reportPmd, "Static"));
        methodMap.put("CheckStyle", new MethodAndTypeAnalysis(this::reportCheckstyle, "Static"));
        methodMap.put("SpotBugs", new MethodAndTypeAnalysis(this::reportSpotBugs, "Binary"));
        methodMap.put("OWASP", new MethodAndTypeAnalysis(this::reportOwasp, "Binary"));
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        String dir = System.getProperty("user.dir") + "/down";

        try {
            GitUtil.cloneRepository(registerTaskDto.getUrl(), dir);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        for (String run : registerTaskDto.getAnalysis()) {
            report = (Report) methodMap.get(run).getFunction().apply(registerTaskDto);
        }
        DeleteFileUtil.deleteDir(new File(dir));

    }

    private Report reportOwasp(RegisterTaskDto registerTaskDto) {
    return null;
    }

    private Report reportSpotBugs(RegisterTaskDto registerTaskDto) {
        return null;
    }

    private Report reportCheckstyle(RegisterTaskDto registerTaskDto) {
        return null;
    }

    private Report reportPmd(RegisterTaskDto registerTaskDto) {
        return null;
    }

}
