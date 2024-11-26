package org.github.babkiniaa.scas.service;

import io.micrometer.core.instrument.MeterRegistry;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.Response.ReportAndIdProjectDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final MeterRegistry meterRegistry;


    /**
     * количество проектов у пользователя установить
     **/
    public void userAnalysis(long userId1, int a) {
        String userId = String.valueOf(userId1);

        meterRegistry.counter("project_count_user", "user_project", userId).increment(a);
        countProject();
    }

    /**
     * количество тасок у пользователя установить
     **/
    public void userTask(long userId1, int a) {
        String userId = String.valueOf(userId1);

        meterRegistry.counter("task_count_user", "user_task", userId).increment(a);
        countAnalyze();
    }

    /**
     * количество репортов у пользователя установить
     **/
    public void userReport(long userId1, int a, ReportAndIdProjectDto reportAndIdProjectDto) {
        String userId = String.valueOf(userId1);

        meterRegistry.counter("report_count_user", "report_task", userId).increment(a);

        countAllReport();
        projectAnalyzeOne(reportAndIdProjectDto);
        projectAnalyzeMore(reportAndIdProjectDto);
    }

    /**
     * количество запускав анализатора одного по каждому проекту
     **/
    public void projectAnalyzeOne(ReportAndIdProjectDto reportAndIdProjectDto) {
        String projectId = String.valueOf(reportAndIdProjectDto.getProjectId());

        if (reportAndIdProjectDto.getBugInstanceCustoms().size() > 0) {
            meterRegistry.counter("spot_count_project", "spot_task", projectId).increment();
        }
        if (reportAndIdProjectDto.getDependencyCustoms().size() > 0) {
            meterRegistry.counter("owasp_count_project", "owasp_task", projectId).increment();
        }
        if (reportAndIdProjectDto.getRuleViolationCustoms().size() > 0) {
            meterRegistry.counter("pmd_count_project", "pmd_task", projectId).increment();
        }
        if (reportAndIdProjectDto.getViolationCustoms().size() > 0) {
            meterRegistry.counter("check_count_project", "check_task", projectId).increment();
        }
        countStartAnalyzer(reportAndIdProjectDto);
    }

    /**
     * количество всех анализаторов проекту
     **/
    public void projectAnalyzeMore(ReportAndIdProjectDto reportAndIdProjectDto) {
        String projectId = String.valueOf(reportAndIdProjectDto.getProjectId());

        if (reportAndIdProjectDto.getBugInstanceCustoms().size() > 0) {
            meterRegistry.counter("all_analyze_count_project", "all_analyze_task", projectId).increment();
        }
        if (reportAndIdProjectDto.getDependencyCustoms().size() > 0) {
            meterRegistry.counter("all_analyze_count_project", "all_analyze_task", projectId).increment();
        }
        if (reportAndIdProjectDto.getRuleViolationCustoms().size() > 0) {
            meterRegistry.counter("all_analyze_count_project", "all_analyze_task", projectId).increment();
        }
        if (reportAndIdProjectDto.getViolationCustoms().size() > 0) {
            meterRegistry.counter("all_analyze_count_project", "all_analyze_task", projectId).increment();
        }
    }

    /**
     * количество багов общих в report
     **/
    public void bagsInAnalyze(ReportAndIdProjectDto reportAndIdProjectDto, long reportId1) {
        String reportId = String.valueOf(reportId1);

        if (reportAndIdProjectDto.getBugInstanceCustoms().size() > 0) {
            meterRegistry.counter("all_bags_count_report", "all_bags_report", reportId).increment(reportAndIdProjectDto.getBugInstanceCustoms().size());
        }
        if (reportAndIdProjectDto.getDependencyCustoms().size() > 0) {
            meterRegistry.counter("all_bags_count_report", "all_bags_report", reportId).increment(reportAndIdProjectDto.getDependencyCustoms().size());
        }
        if (reportAndIdProjectDto.getRuleViolationCustoms().size() > 0) {
            meterRegistry.counter("all_bags_count_report", "all_bags_report", reportId).increment(reportAndIdProjectDto.getRuleViolationCustoms().size());
        }
        if (reportAndIdProjectDto.getViolationCustoms().size() > 0) {
            meterRegistry.counter("all_bags_count_report", "all_bags_report", reportId).increment(reportAndIdProjectDto.getViolationCustoms().size());
        }
        countBagsInAnalyze(reportAndIdProjectDto, reportId1);
    }

    /**
     * количество багов по каждому анализу в report
     **/
    public void countBagsInAnalyze(ReportAndIdProjectDto reportAndIdProjectDto, long reportId1) {
        String reportId = String.valueOf(reportId1);

        if (reportAndIdProjectDto.getBugInstanceCustoms().size() > 0) {
            meterRegistry.counter("count_spot_count_report", "count_spot_report", reportId).increment(reportAndIdProjectDto.getBugInstanceCustoms().size());
        }
        if (reportAndIdProjectDto.getDependencyCustoms().size() > 0) {
            meterRegistry.counter("count_owasp_count_report", "count_owasp_report", reportId).increment(reportAndIdProjectDto.getDependencyCustoms().size());
        }
        if (reportAndIdProjectDto.getRuleViolationCustoms().size() > 0) {
            meterRegistry.counter("count_pmd_count_report", "count_pmd_report", reportId).increment(reportAndIdProjectDto.getRuleViolationCustoms().size());
        }
        if (reportAndIdProjectDto.getViolationCustoms().size() > 0) {
            meterRegistry.counter("count_checkstyle_count_report", "count_checkstyle_report", reportId).increment(reportAndIdProjectDto.getViolationCustoms().size());
        }
    }

    /**
     * количество созданных проектов в целом для админа 🦸‍♂️
     **/
    public void countProject() {
        meterRegistry.counter("all_project_count", "all_project", "admin").increment();
    }

    /**
     * сколько раз был запущен анализ 🦸‍♂️
     **/
    public void countAnalyze() {
        meterRegistry.counter("all_analyze_count", "all_analyze", "admin").increment();
    }

    /**
     * сколько каждый анализатор вообще отработал 🦸‍♂️
     **/
    public void countStartAnalyzer(ReportAndIdProjectDto reportAndIdProjectDto) {
        if (reportAndIdProjectDto.getBugInstanceCustoms().size() > 0) {
            meterRegistry.counter("all_spot_count_project", "all_spot_task", "admin").increment();
        }
        if (reportAndIdProjectDto.getDependencyCustoms().size() > 0) {
            meterRegistry.counter("all_owasp_count_project", "all_owasp_task", "admin").increment();
        }
        if (reportAndIdProjectDto.getRuleViolationCustoms().size() > 0) {
            meterRegistry.counter("all_pmd_count_project", "all_pmd_task", "admin").increment();
        }
        if (reportAndIdProjectDto.getViolationCustoms().size() > 0) {
            meterRegistry.counter("all_check_count_project", "all_check_task", "admin").increment();
        }
    }


    /**
     * сколько всего было создано тасок 🦸‍♂️
     **/
    public void countAllReport() {
        meterRegistry.counter("all_report_count", "all_report", "admin").increment();
    }
}
