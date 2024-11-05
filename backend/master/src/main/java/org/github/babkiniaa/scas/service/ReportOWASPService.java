package org.github.babkiniaa.scas.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ReportOWASPMapper;
import org.github.babkiniaa.scas.dto.ReportOWASPDto;
import org.github.babkiniaa.scas.entity.ReportOWASP;
import org.github.babkiniaa.scas.entity.reportsEntity.DependencyCustom;
import org.github.babkiniaa.scas.repository.ReportOWASPRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportOWASPService {
    private final ReportOWASPRepository ReportOWASPRepository;

    private final ReportOWASPMapper ReportOWASPMapper;

    public int create(ReportOWASPDto ReportOWASPDto) {
        return ReportOWASPRepository.save(ReportOWASPMapper.reportToEntity(ReportOWASPDto)).getId();
    }

    public List<ReportOWASP> findAll() {
        return ReportOWASPRepository.findAll();
    }

    public ReportOWASP update(ReportOWASPDto ReportOWASPDto) {
        ReportOWASP ReportOWASP = ReportOWASPMapper.reportToEntity(ReportOWASPDto);
        return ReportOWASPRepository.save(ReportOWASP);
    }

    public void delete(Integer id) {
        Optional<ReportOWASP> ReportOWASPOpt = ReportOWASPRepository.findById(id);

        if (ReportOWASPOpt.isPresent()) {
            ReportOWASPRepository.delete(ReportOWASPOpt.get());
        } else {
            throw new EntityNotFoundException("ReportOWASP with ID " + id + " not found.");
        }
    }

    public Optional<ReportOWASP> findById(Integer id) {
        return ReportOWASPRepository.findById(id);
    }

}
