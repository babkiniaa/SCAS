package org.github.babkiniaa.scas.client;

import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * The interface Agent client.
 */
@FeignClient(name = "master-service", url = "http://localhost:8082")
public interface AgentClient {

    /**
     * Save in master report response entity.
     *
     * @param projectId the project id
     * @param reportDto the report dto
     * @return the response entity
     */
    @PostMapping("/report/save/{id}")
    ResponseEntity<?> saveInMasterReport(@PathVariable("id") long projectId, @RequestBody ReportDto reportDto);

}
