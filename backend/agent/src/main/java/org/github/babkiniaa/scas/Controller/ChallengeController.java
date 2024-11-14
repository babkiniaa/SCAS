package org.github.babkiniaa.scas.Controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.entity.StatusTask;
import org.github.babkiniaa.scas.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * The type Challenge controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class ChallengeController {

    private final TaskService taskService;

    /**
     * Init task long.
     *
     * @param registerTaskDto the register task dto
     * @return the long
     */
    @PostMapping("/start/queue")
    public Long initTask(@RequestBody RegisterTaskDto registerTaskDto) {
        return taskService.saveTask(registerTaskDto);
    }

    /**
     * Gets method map.
     *
     * @return the method map
     */
    @GetMapping("/get-hashmap")
    public HashMap<String, List<String>> getMethodMap() {
        return taskService.getMethodMap();
    }

    /**
     * Gets status.
     *
     * @param projectId the project id
     * @return the status
     */
    @GetMapping("/task/{id}/status")
    public String getStatus(@PathVariable("id") long projectId) {
        try {
            return taskService.getStatusByProjectId(projectId).toString();
        } catch (Exception e) {
            return StatusTask.NotFound.toString();
        }

    }

}
