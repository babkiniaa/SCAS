package org.github.babkiniaa.scas.service;

import io.micrometer.core.instrument.MeterRegistry;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
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
        meterRegistry.counter("project_count_user", "userId", userId).increment(a);
        countProject();
    }

    /**
     * количество тасок у пользователя установить
     **/
    public void userTask(long userId1, int a) {
        String userId = String.valueOf(userId1);

        meterRegistry.counter("task_count_user", "userId", userId).increment(a);
        countAnalyze();
    }

    /**
     * количество репортов у пользователя установить
     **/
    public void userReport(long userId1, int a, ReportAndIdProjectDto reportAndIdProjectDto) {
        String userId = String.valueOf(userId1);

        meterRegistry.counter("report_count_user", "userId", userId).increment(a);
        countAllReport();
        projectAnalyzeOne(reportAndIdProjectDto);
        projectAnalyzeMore(reportAndIdProjectDto);
    }

    /**
     * количество запускав анализатора одного по каждому проекту
     **/
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

    /**
     * количество всех анализаторов проекту
     **/
    public void projectAnalyzeMore(ReportAndIdProjectDto reportAndIdProjectDto) {
        String projectId = String.valueOf(reportAndIdProjectDto.getProjectId());

        if ((reportAndIdProjectDto.getBugInstanceCustoms() != null) && reportAndIdProjectDto.getBugInstanceCustoms().size() > 0) {
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

    /**
     * количество багов общих в report
     **/
    public void bagsInAnalyze(long userId1, ReportAndIdProjectDto reportAndIdProjectDto, long reportId1) {
        String reportId = String.valueOf(reportId1);
        String userId = String.valueOf(userId1);
        String branch = reportAndIdProjectDto.getBranch();

        if(branch == null){
            branch = "all";
        }
        if ((reportAndIdProjectDto.getBugInstanceCustoms() != null) && reportAndIdProjectDto.getBugInstanceCustoms().size() > 0) {
            meterRegistry.counter("all_bags_count_report", "reportId", reportId).increment(reportAndIdProjectDto.getBugInstanceCustoms().size());
            meterRegistry.counter("report_branch_count_user", "userId", userId, "branch", branch, "projectId", String.valueOf(reportAndIdProjectDto.getProjectId())).increment(reportAndIdProjectDto.getBugInstanceCustoms().size());
        }
        if ((reportAndIdProjectDto.getDependencyCustoms() != null) && reportAndIdProjectDto.getDependencyCustoms().size() > 0) {
            meterRegistry.counter("all_bags_count_report", "reportId", reportId).increment(reportAndIdProjectDto.getDependencyCustoms().size());
            meterRegistry.counter("report_branch_count_user", "userId", userId, "branch", branch, "projectId", String.valueOf(reportAndIdProjectDto.getProjectId())).increment(reportAndIdProjectDto.getDependencyCustoms().size());
        }
        if ((reportAndIdProjectDto.getRuleViolationCustoms() != null) && reportAndIdProjectDto.getRuleViolationCustoms().size() > 0) {
            meterRegistry.counter("all_bags_count_report", "reportId", reportId).increment(reportAndIdProjectDto.getRuleViolationCustoms().size());
            meterRegistry.counter("report_branch_count_user", "userId", userId, "branch", branch, "projectId", String.valueOf(reportAndIdProjectDto.getProjectId())).increment(reportAndIdProjectDto.getRuleViolationCustoms().size());;
        }
        if ((reportAndIdProjectDto.getViolationCustoms() != null) && reportAndIdProjectDto.getViolationCustoms().size() > 0) {
            meterRegistry.counter("all_bags_count_report", "reportId", reportId).increment(reportAndIdProjectDto.getViolationCustoms().size());
            meterRegistry.counter("report_branch_count_user", "userId", userId, "branch", branch, "projectId", String.valueOf(reportAndIdProjectDto.getProjectId())).increment(reportAndIdProjectDto.getViolationCustoms().size());;
        }
        countBagsInAnalyze(reportAndIdProjectDto, reportId1);
    }

    /**
     * количество багов по каждому анализу в report
     **/
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

    /**
     * количество созданных проектов в целом для админа 🦸‍♂️
     **/
    public void countProject() {
        meterRegistry.counter("all_project_count", "admin", "admin").increment();
    }

    /**
     * сколько раз был запущен анализ 🦸‍♂️
     **/
    public void countAnalyze() {
        meterRegistry.counter("all_analyze_count", "admin", "admin").increment();
    }

    /**
     * сколько каждый анализатор вообще отработал 🦸‍♂️
     **/
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


    /**
     * сколько всего было создано тасок 🦸‍♂️
     **/
    public void countAllReport() {
        meterRegistry.counter("all_report_count", "admin", "admin").increment();
    }
}
