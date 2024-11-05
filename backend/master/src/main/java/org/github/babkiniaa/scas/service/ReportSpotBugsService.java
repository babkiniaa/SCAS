package org.github.babkiniaa.scas.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ReportSpotBugsMapper;
import org.github.babkiniaa.scas.dto.ReportSpotBugsDto;
import org.github.babkiniaa.scas.entity.ReportSpotBugs;
import org.github.babkiniaa.scas.entity.reportsEntity.BugInstanceCustom;
import org.github.babkiniaa.scas.repository.ReportSpotBugsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportSpotBugsService {
    private final ReportSpotBugsRepository ReportSpotBugsRepository;

    private final ReportSpotBugsMapper ReportSpotBugsMapper;

    public int create(ReportSpotBugsDto ReportSpotBugsDto) {
        return ReportSpotBugsRepository.save(ReportSpotBugsMapper.reportToEntity(ReportSpotBugsDto)).getId();
    }

    public List<ReportSpotBugs> findAll() {
        return ReportSpotBugsRepository.findAll();
    }

    public ReportSpotBugs update(ReportSpotBugsDto ReportSpotBugsDto) {
        ReportSpotBugs ReportSpotBugs = ReportSpotBugsMapper.reportToEntity(ReportSpotBugsDto);
        return ReportSpotBugsRepository.save(ReportSpotBugs);
    }

    public void delete(Integer id) {
        Optional<ReportSpotBugs> ReportSpotBugsOpt = ReportSpotBugsRepository.findById(id);

        if (ReportSpotBugsOpt.isPresent()) {
            ReportSpotBugsRepository.delete(ReportSpotBugsOpt.get());
        } else {
            throw new EntityNotFoundException("ReportSpotBugs with ID " + id + " not found.");
        }
    }

    public Optional<ReportSpotBugs> findById(Integer id) {
        return ReportSpotBugsRepository.findById(id);
    }
}
