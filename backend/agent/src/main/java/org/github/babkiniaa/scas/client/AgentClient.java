package org.github.babkiniaa.scas.client;

import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The interface Agent client.
 */
@FeignClient(name = "master-service", url = "https://master-img-production.up.railway.app")
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

    @GetMapping("/report/get-analyzers")
    List<String> getAnalyzers(@RequestParam String hash, @RequestParam long projectId);

}
