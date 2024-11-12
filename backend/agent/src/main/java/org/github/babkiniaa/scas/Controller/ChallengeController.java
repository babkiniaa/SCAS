package org.github.babkiniaa.scas.Controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.entity.StatusTask;
import org.github.babkiniaa.scas.mapper.*;
import org.github.babkiniaa.scas.service.TaskService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class ChallengeController {

    private final TaskService taskService;

    @PostMapping("/start/queue")
    public Long initTask(@RequestBody RegisterTaskDto registerTaskDto) throws Exception {
        return taskService.saveTask(registerTaskDto);
    }

    @GetMapping("/task/{id}/status")
    public String getStatus(@PathVariable("id") long projectId) throws Exception {
        return taskService.getStatusByProjectId(projectId).toString();
    }

//    @GetMapping("/get-hashmap")
//    public HashMap<String, List<String>> getMethodMap(){
//        HashMap<String, List<String>> hashMap = new HashMap<>();
//
//        for(String  m: methodMap.keySet()){
//            hashMap.computeIfAbsent(methodMap.get(m).getType().toLowerCase(), k -> new ArrayList<>()).add(m);
//        }
//
//        return hashMap;
//    }

    @GetMapping("/task/{id}/report")
    public ReportDto getReport(@PathVariable("id") long projectId){
        return taskService.getReportByProjectId(projectId);
    }

}
