package org.example.service;

import lombok.AllArgsConstructor;
import org.example.Mapper.ReportMapper;
import org.example.dto.ReportDto;
import org.example.entity.Report;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportService {
    @Autowired
    private final ReportRepository reportRepository;
    @Autowired
    private final ReportMapper reportMapper;

    public Report create(ReportDto reportDto) {
        return reportRepository.save(reportMapper.reportToEntity(reportDto));
    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public Report update(ReportDto reportDto) {
        return reportRepository.save(reportMapper.reportToEntity(reportDto));
    }

    public Optional<Report> findById(Integer id) {
        return reportRepository.findById(id);
    }

    public void create(Report reportToEntity) {
    }
}
