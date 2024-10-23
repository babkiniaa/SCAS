package org.github.babkiniaa.scas.service;

import lombok.AllArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ReportPmdMapper;
import org.github.babkiniaa.scas.dto.ReportPmdDto;
import org.github.babkiniaa.scas.entity.ReportPmd;
import org.github.babkiniaa.scas.repository.ReportPmdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportPmdService {
    @Autowired
    private final ReportPmdRepository reportPmdRepository;
    @Autowired
    private final ReportPmdMapper reportPmdMapper;

    public ReportPmd create(ReportPmdDto reportPmdDto) {
        return reportPmdRepository.save(reportPmdMapper.reportPmdToEntity(reportPmdDto));
    }

    public List<ReportPmd> findAll() {
        return reportPmdRepository.findAll();
    }

    public ReportPmd update(ReportPmdDto reportPmdDto) {
        return reportPmdRepository.save(reportPmdMapper.reportPmdToEntity(reportPmdDto));
    }

    public Optional<ReportPmd> findById(Integer id) {
        return reportPmdRepository.findById(id);
    }
}
