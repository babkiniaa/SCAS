package org.github.babkiniaa.scas.client;

import org.github.babkiniaa.scas.dto.typeForMap.MethodAndTypeAnalysis;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;

@FeignClient(name = "agent-service", url = "http://localhost:8081")
public interface AgentServiceClient {

    @GetMapping("analysis/get-hashmap")
    HashMap<String, List<String>>  getMethodMap();

}
