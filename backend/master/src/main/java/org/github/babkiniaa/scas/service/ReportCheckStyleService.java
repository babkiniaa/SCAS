package org.github.babkiniaa.scas.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ReportCheckStyleMapper;
import org.github.babkiniaa.scas.dto.ReportCheckStyleDto;
import org.github.babkiniaa.scas.entity.ReportCheckStyle;
import org.github.babkiniaa.scas.repository.ReportCheckStyleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportCheckStyleService {
    private final ReportCheckStyleRepository ReportCheckStyleRepository;

    private final ReportCheckStyleMapper ReportCheckStyleMapper;

    public int create(ReportCheckStyleDto ReportCheckStyleDto) {
        return ReportCheckStyleRepository.save(ReportCheckStyleMapper.reportToEntity(ReportCheckStyleDto)).getId();
    }

    public List<ReportCheckStyle> findAll() {
        return ReportCheckStyleRepository.findAll();
    }

    public ReportCheckStyle update(ReportCheckStyleDto ReportCheckStyleDto) {
        ReportCheckStyle ReportCheckStyle = ReportCheckStyleMapper.reportToEntity(ReportCheckStyleDto);
        return ReportCheckStyleRepository.save(ReportCheckStyle);
    }

    public void delete(Integer id) {
        Optional<ReportCheckStyle> ReportCheckStyleOpt = ReportCheckStyleRepository.findById(id);

        if (ReportCheckStyleOpt.isPresent()) {
            ReportCheckStyleRepository.delete(ReportCheckStyleOpt.get());
        } else {
            throw new EntityNotFoundException("ReportCheckStyle with ID " + id + " not found.");
        }
    }

    public ReportCheckStyle findById(Integer id) {
        return ReportCheckStyleRepository.findById(id).get();
    }
}
