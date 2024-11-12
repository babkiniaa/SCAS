package org.github.babkiniaa.scas.client;

import org.github.babkiniaa.scas.dto.project.ProjectDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "agent-service", url = "http://localhost:8081")
public interface AgentServiceClient {

    @PostMapping("/analysis/init")
    ProjectDto init(@RequestBody ProjectDto projectDto);

}
