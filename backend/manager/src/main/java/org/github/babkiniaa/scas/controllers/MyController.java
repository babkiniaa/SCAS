package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.MasterServiceClient;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MyController {

    private MasterServiceClient agentServiceClient;

    @PostMapping("/analysis/init")
    public ResponseEntity<?> createReport(@RequestBody ProjectDto projectDto, @RequestBody int projectId) {
        return agentServiceClient.createReport(projectDto, projectId);
    }
}
