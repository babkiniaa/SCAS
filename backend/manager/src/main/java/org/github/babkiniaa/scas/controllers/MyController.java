package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.client.MasterServiceClient;
import org.github.babkiniaa.scas.dto.ListReportDto;
import org.github.babkiniaa.scas.dto.forUserDto.LoginDto;
import org.github.babkiniaa.scas.dto.project.*;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.exception.NotFoundUserException;
import org.github.babkiniaa.scas.security.AuthenticationFacade;
import org.github.babkiniaa.scas.security.JwtTokenFilter;
import org.github.babkiniaa.scas.security.JwtTokenProvider;
import org.github.babkiniaa.scas.service.AuthService;
import org.github.babkiniaa.scas.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MyController {

    private final MasterServiceClient masterServiceClient;
    private final AgentServiceClient agentServiceClient;
    private final UserService userService;
    private final AuthService authService;
    private final AuthenticationFacade authenticationFacade;
    private final JwtTokenFilter jwtTokenFilter;


    @PostMapping("/report/create/offline")
    public ResponseEntity<?> createJWTReport(@RequestBody AnalyserDto analyserDto) throws NotFoundUserException {
        long reportId = masterServiceClient.createReport(analyserDto);
        Optional<User> user = userService.findByUsername(authenticationFacade.getCurrentUserName());

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("user is empty");
        }
        LoginDto loginDto = new LoginDto(user.get().getPassword(), user.get().getUsername());

        return ResponseEntity.ok(authService.loginAndReport(loginDto, reportId, analyserDto.getIdProject()));
    }

    @PostMapping("/report/start/offline")
    public ResponseEntity<?> startJWTReport(@RequestBody StringDto token) {
        ProjectIdAndReportId projectIdAndReportId = new ProjectIdAndReportId(jwtTokenFilter.getReportId(token.getToken()), jwtTokenFilter.getProjectId(token.getToken()));

        masterServiceClient.createReportOffline(projectIdAndReportId);
        return ResponseEntity.ok("work");
    }

    @GetMapping("analysis/get-hashmap")
    public HashMap<String, List<String>> getMethodMap() {

        return agentServiceClient.getMethodMap();
    }


    @PostMapping("/report/create")
    public long createReport(@RequestBody AnalyserDto analyserDto) {
        return masterServiceClient.createReport(analyserDto);
    }

    @GetMapping("report/get-by-project/{id}")
    public List<ListReportDto> findReportsByProjectId(@PathVariable("id") long projectId) {
        return masterServiceClient.findReportsByProjectId(projectId);
    }

    @GetMapping("report/status/{id}")
    public String getStatus(@PathVariable("id") long idTask) {
        return masterServiceClient.getStatus(idTask);
    }

    @GetMapping("report/get-reports/{id}")
    ReportDto getRep(@PathVariable("id") long projectId) {
        return masterServiceClient.getRep(projectId);
    }

    @GetMapping("report/find/{id}")
    public ReportDto getReport(@PathVariable("id") Long idReport) {
        return masterServiceClient.getReport(idReport);
    }


    @PostMapping("/project/create")
    public long createProject(@RequestBody ProjectCreateDto projectCreateDto) {

        return masterServiceClient.createProject(projectCreateDto);
    }

    @PostMapping("/project/get-projects")
    public List<ProjectDto> getProject(@RequestBody GetProjectAllDto projectsDto) {

        return masterServiceClient.getProject(projectsDto);
    }

    @GetMapping("project/get-project/{id}")
    public ProjectDto getProject(@PathVariable("id") int id) {

        return masterServiceClient.getProject(id);
    }

    @GetMapping("project/get-all")
    public List<ProjectDto> getAllProject() {

        return masterServiceClient.getAllProject();
    }


}
