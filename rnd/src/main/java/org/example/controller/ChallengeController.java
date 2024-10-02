package org.example.controller;

import org.example.entity.Report;
import org.example.repository.ReportRepository;
import org.example.textReader.Manager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

@RestController
public class ChallengeController {

    private final Manager manager;
    private final ReportRepository reportRepository;
    private final ExecutorService executorService;

    public ChallengeController(Manager manager, ExecutorService executorService, ReportRepository reportRepository) {
        this.manager = manager;
        this.executorService = executorService;
        this.reportRepository = reportRepository;
    }

    @GetMapping("/repors")
    public List<Report> allReports() {
        return reportRepository.findAll();
    }

    //    Многопоточная версия
    @PostMapping("/api/main")
    public ResponseEntity<String> CallWork(@RequestParam String url) {

        executorService.submit(() -> {
            try {
                manager.setUrl(url);
                manager.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
//        reportRepository.save();
        return ResponseEntity.ok("Работаем в фоне");
    }

//    Обычная версия
    //    @GetMapping("/api/main")
//    public ResponseEntity<String> CallWork(@RequestParam String url) {
//        manager.setUrl(url);
//        try {
//            manager.start();
//            return ResponseEntity.ok("{\"message\": \"Работаем\"}");
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Ошибка при запуске менеджера\"}");
//        }
//    }
}
