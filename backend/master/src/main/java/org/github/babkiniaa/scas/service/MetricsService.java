package org.github.babkiniaa.scas.service;

import io.micrometer.core.instrument.MeterRegistry;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.github.babkiniaa.scas.dto.Response.ReportAndIdProjectDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final MeterRegistry meterRegistry;
    private final Map<String, AtomicInteger> bugCountGauges = new ConcurrentHashMap<>();

    public void userAnalysis(long userId1, int a) {
        String userId = String.valueOf(userId1);
        meterRegistry.counter("project_count_user", "userId", userId).increment(a);
        countProject();
    }


    public void userTask(long userId1, int a) {
        String userId = String.valueOf(userId1);

        meterRegistry.counter("task_count_user", "userId", userId).increment(a);
        countAnalyze();
    }


    public void userReport(long userId1, int a, ReportAndIdProjectDto reportAndIdProjectDto) {
        String userId = String.valueOf(userId1);

        meterRegistry.counter("report_count_user", "userId", userId).increment(a);
        countAllReport();
        projectAnalyzeOne(reportAndIdProjectDto);
        projectAnalyzeMore(reportAndIdProjectDto);
    }


    public void projectAnalyzeOne(ReportAndIdProjectDto reportAndIdProjectDto) {
        String projectId = String.valueOf(reportAndIdProjectDto.getProjectId());

        if ((reportAndIdProjectDto.getBugInstanceCustoms() != null) && reportAndIdProjectDto.getBugInstanceCustoms().size() > 0) {
            meterRegistry.counter("spot_count_project", "projectId", projectId).increment();
        }
        if ((reportAndIdProjectDto.getDependencyCustoms() != null) && reportAndIdProjectDto.getDependencyCustoms().size() > 0) {
            meterRegistry.counter("owasp_count_project", "projectId", projectId).increment();
        }
        if ((reportAndIdProjectDto.getRuleViolationCustoms() != null) && reportAndIdProjectDto.getRuleViolationCustoms().size() > 0) {
            meterRegistry.counter("pmd_count_project", "projectId", projectId).increment();
        }
        if ((reportAndIdProjectDto.getViolationCustoms() != null) && reportAndIdProjectDto.getViolationCustoms().size() > 0) {
            meterRegistry.counter("check_count_project", "projectId", projectId).increment();
        }
        countStartAnalyzer(reportAndIdProjectDto);
    }


    public void projectAnalyzeMore(ReportAndIdProjectDto reportAndIdProjectDto) {
        String projectId = String.valueOf(reportAndIdProjectDto.getProjectId());

        if ((reportAndIdProjectDto.getBugInstanceCustoms() != null) && (reportAndIdProjectDto.getBugInstanceCustoms().size() > 0)) {
            meterRegistry.counter("all_analyze_count_project", "projectId", projectId).increment();
        }
        if ((reportAndIdProjectDto.getDependencyCustoms() != null) && reportAndIdProjectDto.getDependencyCustoms().size() > 0) {
            meterRegistry.counter("all_analyze_count_project", "projectId", projectId).increment();
        }
        if ((reportAndIdProjectDto.getRuleViolationCustoms() != null) && reportAndIdProjectDto.getRuleViolationCustoms().size() > 0) {
            meterRegistry.counter("all_analyze_count_project", "projectId", projectId).increment();
        }
        if ((reportAndIdProjectDto.getViolationCustoms() != null) && reportAndIdProjectDto.getViolationCustoms().size() > 0) {
            meterRegistry.counter("all_analyze_count_project", "projectId", projectId).increment();
        }
    }


    public void bagsInAnalyze(long userId1, ReportAndIdProjectDto reportAndIdProjectDto, long reportId1) {
        String userId = String.valueOf(userId1);
        String branch = reportAndIdProjectDto.getBranch();
        if (branch == null) {
            branch = "all";
        }
        String projectId = String.valueOf(reportAndIdProjectDto.getProjectId());
        meterRegistry.counter("all_bags_count_project", "projectId", projectId).increment();
        updateGaugeWithTags(
                "report_branch_count_user",
                Tags.of("userId", userId, "branch", branch, "projectId", projectId),
                calculateTotalCount(reportAndIdProjectDto)
        );
    }


    private void updateGaugeWithTags(String gaugeName, Tags tags, int value) {
        String key = generateGaugeKey(gaugeName, tags);
        AtomicInteger gaugeValue = bugCountGauges.computeIfAbsent(key, k -> {
            AtomicInteger newGauge = new AtomicInteger(0);
            meterRegistry.gauge(gaugeName, tags, newGauge, AtomicInteger::get);
            return newGauge;
        });
        gaugeValue.set(value);
    }

    private String generateGaugeKey(String gaugeName, Tags tags) {
        return gaugeName + tags.stream()
                .map(tag -> tag.getKey() + "=" + tag.getValue())
                .collect(Collectors.joining(","));
    }

    private int calculateTotalCount(ReportAndIdProjectDto reportAndIdProjectDto) {
        int total = 0;
        if (reportAndIdProjectDto.getBugInstanceCustoms() != null) {
            total += reportAndIdProjectDto.getBugInstanceCustoms().size();
        }
        if (reportAndIdProjectDto.getDependencyCustoms() != null) {
            total += reportAndIdProjectDto.getDependencyCustoms().size();
        }
        if (reportAndIdProjectDto.getRuleViolationCustoms() != null) {
            total += reportAndIdProjectDto.getRuleViolationCustoms().size();
        }
        if (reportAndIdProjectDto.getViolationCustoms() != null) {
            total += reportAndIdProjectDto.getViolationCustoms().size();
        }

        return total;
    }


    public void countBagsInAnalyze(ReportAndIdProjectDto reportAndIdProjectDto, long reportId1) {
        String reportId = String.valueOf(reportId1);

        if ((reportAndIdProjectDto.getBugInstanceCustoms() != null) && reportAndIdProjectDto.getBugInstanceCustoms().size() > 0) {
            meterRegistry.counter("count_spot_count_report", "reportId", reportId).increment(reportAndIdProjectDto.getBugInstanceCustoms().size());
        }
        if ((reportAndIdProjectDto.getDependencyCustoms() != null) && reportAndIdProjectDto.getDependencyCustoms().size() > 0) {
            meterRegistry.counter("count_owasp_count_report", "reportId", reportId).increment(reportAndIdProjectDto.getDependencyCustoms().size());
        }
        if ((reportAndIdProjectDto.getRuleViolationCustoms() != null) && reportAndIdProjectDto.getRuleViolationCustoms().size() > 0) {
            meterRegistry.counter("count_pmd_count_report", "reportId", reportId).increment(reportAndIdProjectDto.getRuleViolationCustoms().size());
        }
        if ((reportAndIdProjectDto.getViolationCustoms() != null) && reportAndIdProjectDto.getViolationCustoms().size() > 0) {
            meterRegistry.counter("count_checkstyle_count_report", "reportId", reportId).increment(reportAndIdProjectDto.getViolationCustoms().size());
        }
    }


    public void countProject() {
        meterRegistry.counter("all_project_count", "admin", "admin").increment();
    }


    public void countAnalyze() {
        meterRegistry.counter("all_analyze_count", "admin", "admin").increment();
    }


    public void countStartAnalyzer(ReportAndIdProjectDto reportAndIdProjectDto) {
        if ((reportAndIdProjectDto.getBugInstanceCustoms() != null) && reportAndIdProjectDto.getBugInstanceCustoms().size() > 0) {
            meterRegistry.counter("all_spot_count_project", "admin", "admin").increment();
        }
        if ((reportAndIdProjectDto.getDependencyCustoms() != null) && reportAndIdProjectDto.getDependencyCustoms().size() > 0) {
            meterRegistry.counter("all_owasp_count_project", "admin", "admin").increment();
        }
        if ((reportAndIdProjectDto.getRuleViolationCustoms() != null) && reportAndIdProjectDto.getRuleViolationCustoms().size() > 0) {
            meterRegistry.counter("all_pmd_count_project", "admin", "admin").increment();
        }
        if ((reportAndIdProjectDto.getViolationCustoms() != null) && reportAndIdProjectDto.getViolationCustoms().size() > 0) {
            meterRegistry.counter("all_check_count_project", "admin", "admin").increment();
        }
    }


    public void countAllReport() {
        meterRegistry.counter("all_report_count", "admin", "admin").increment();
    }
}
