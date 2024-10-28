package org.github.babkiniaa.scas.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import net.sourceforge.pmd.reporting.RuleViolation;
import org.github.babkiniaa.scas.Mapper.ReportPMDMapper;
import org.github.babkiniaa.scas.dto.ReportPMDDto;
import org.github.babkiniaa.scas.entity.ReportPMD;
import org.github.babkiniaa.scas.repository.ReportPMDRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportPMDService {
    private final ReportPMDRepository ReportPMDRepository;

    private final ReportPMDMapper ReportPMDMapper;

    public ReportPMD create(ReportPMDDto ReportPMDDto) {
        return ReportPMDRepository.save(ReportPMDMapper.reportToEntity(ReportPMDDto));
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

    public Optional<ReportPMD> findById(Integer id) {
        return ReportPMDRepository.findById(id);
    }

    public ReportPMD updatePmd(Integer id, List<RuleViolation> rep) {
        ReportPMD ReportPMD = new ReportPMD();
        if (!findById(id).isEmpty()) {
            ReportPMD = findById(id).get();
//            ReportPMD.setReportList(rep);
            save(ReportPMD);
        }
        return ReportPMD;
    }

    public ReportPMD save(ReportPMD ReportPMD){
        return ReportPMDRepository.save(ReportPMD);
    }

    public void create(ReportPMD ReportPMDToEntity) {
    }
}
