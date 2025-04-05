package org.github.babkiniaa.scas.service;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ReportMapper;
import org.github.babkiniaa.scas.configuration.TaskQueue;
import org.github.babkiniaa.scas.dto.AnalyserDto;
import org.github.babkiniaa.scas.dto.Response.ReportAndIdProjectDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.repository.ReportRepository;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final MetricsService metricsService;
    private final ProjectService projectService;
    private final KafkaTemplate<String, AnalyserDto> kafkaTemplate;
    private final TaskQueue taskQueue;

    @Transactional
    @KafkaListener(topics = "report-create-events-topic", groupId = "report-created-events")
    public void save(ReportAndIdProjectDto reportAndIdProjectDto) {
        Optional<Report> previous = reportRepository.findByHashAndProjectId(reportAndIdProjectDto.getHash(), reportAndIdProjectDto.getProjectId());
        long userId = projectService.findByIdProject(reportAndIdProjectDto.getProjectId()).getUserId();
        Report reportEntity = new Report();
        if (previous.isEmpty()) {
            metricsService.userReport(userId, 1, reportAndIdProjectDto);
            reportEntity = reportMapper.reportReportAndIdProjectDto(reportAndIdProjectDto);
            reportEntity.setProjectId(reportAndIdProjectDto.getProjectId());
        } else {
            reportEntity = previous.get();
            metricsService.userReport(userId, 1, reportAndIdProjectDto);
            List<String> analyzers = reportAndIdProjectDto.getAnalyzers();
            analyzers.addAll(reportEntity.getAnalyzers());
            reportAndIdProjectDto.setAnalyzers(analyzers);
            reportMapper.updateReportFromDto(reportAndIdProjectDto, reportEntity);
            reportEntity.setCreatedDate(LocalDateTime.now());
        }
        metricsService.bagsInAnalyze(userId, reportAndIdProjectDto, reportRepository.save(reportEntity).getId());
    }

    @Transactional
    @KafkaListener(topics = "task-load-events-topic", groupId = "report-created-events")
    public void runTask(double loadAgent) throws InterruptedException {
        if(loadAgent < 0.9){
            CompletableFuture<SendResult<String, AnalyserDto>> future =
                    kafkaTemplate.send("task-create-events-topic", null, taskQueue.takeTask());
        }
    }
}
