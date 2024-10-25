package org.github.babkiniaa.scas.controllers;

import org.github.babkiniaa.scas.client.MasterServiceClient;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyController {

    @Autowired
    private MasterServiceClient agentServiceClient;

    @PostMapping("/analysis/init")
    public ResponseEntity<?> createReport(@RequestBody ProjectDto projectDto) {
        return agentServiceClient.createReport(projectDto);
    }
}
