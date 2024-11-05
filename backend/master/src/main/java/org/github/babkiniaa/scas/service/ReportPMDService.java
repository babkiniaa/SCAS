package org.github.babkiniaa.scas.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ReportPMDMapper;
import org.github.babkiniaa.scas.dto.ReportPMDDto;
import org.github.babkiniaa.scas.entity.ReportPMD;
import org.github.babkiniaa.scas.entity.reportsEntity.RuleViolationCustom;
import org.github.babkiniaa.scas.repository.ReportPMDRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportPMDService {
    private final ReportPMDRepository ReportPMDRepository;

    private final ReportPMDMapper ReportPMDMapper;

    public int create(ReportPMDDto ReportPMDDto) {
        return ReportPMDRepository.save(ReportPMDMapper.reportToEntity(ReportPMDDto)).getId();
    }

    public List<ReportPMD> findAll() {
        return ReportPMDRepository.findAll();
    }

    public ReportPMD update(ReportPMDDto ReportPMDDto) {
        ReportPMD ReportPMD = ReportPMDMapper.reportToEntity(ReportPMDDto);
        return ReportPMDRepository.save(ReportPMD);
    }

    public void delete(Integer id) {
        Optional<ReportPMD> ReportPMDOpt = ReportPMDRepository.findById(id);

        if (ReportPMDOpt.isPresent()) {
            ReportPMDRepository.delete(ReportPMDOpt.get());
        } else {
            throw new EntityNotFoundException("ReportPMD with ID " + id + " not found.");
        }
    }

    public ReportPMD findById(Integer id) {
        return ReportPMDRepository.findById(id).get();
    }
}
