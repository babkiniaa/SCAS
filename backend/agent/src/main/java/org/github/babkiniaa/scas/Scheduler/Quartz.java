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




    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {



    }


}
