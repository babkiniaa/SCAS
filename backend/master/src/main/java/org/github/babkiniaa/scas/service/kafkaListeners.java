package org.github.babkiniaa.scas.service;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ReportMapper;
import org.github.babkiniaa.scas.dto.Response.ReportAndIdProjectDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.repository.ReportRepository;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@KafkaListener(topics = "report-create-events-topic")
public class kafkaListeners {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final MetricsService metricsService;
    private final ProjectService projectService;

    @KafkaHandler
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
            List<String> analyzers = reportAndIdProjectDto.getAnalyzers();
            analyzers.addAll(reportEntity.getAnalyzers());
            reportAndIdProjectDto.setAnalyzers(analyzers);
            reportMapper.updateReportFromDto(reportAndIdProjectDto, reportEntity);
            reportEntity.setCreatedDate(LocalDateTime.now());
        }
        metricsService.bagsInAnalyze(userId, reportAndIdProjectDto, reportRepository.save(reportEntity).getId());
    }
}
