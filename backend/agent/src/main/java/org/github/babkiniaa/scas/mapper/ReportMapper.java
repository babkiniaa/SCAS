package org.github.babkiniaa.scas.mapper;

import org.github.babkiniaa.scas.dto.Response.Report;
import org.github.babkiniaa.scas.dto.Response.ReportAndDir;

public interface ReportMapper {
    Report reportToReportAndDir(ReportAndDir reportAndDir);

    ReportAndDir reportAndDirToReport(Report report);
}
