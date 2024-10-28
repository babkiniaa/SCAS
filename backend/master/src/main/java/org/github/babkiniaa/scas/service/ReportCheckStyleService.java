package org.github.babkiniaa.scas.service;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
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

    public ReportCheckStyle create(ReportCheckStyleDto ReportCheckStyleDto) {
        return ReportCheckStyleRepository.save(ReportCheckStyleMapper.reportToEntity(ReportCheckStyleDto));
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

    public Optional<ReportCheckStyle> findById(Integer id) {
        return ReportCheckStyleRepository.findById(id);
    }

    public ReportCheckStyle updateCheckStyle(Integer id, List<AuditEvent> rep) {
        ReportCheckStyle ReportCheckStyle = new ReportCheckStyle();
        if (!findById(id).isEmpty()) {
            ReportCheckStyle = findById(id).get();
//            ReportCheckStyle.setReportList(rep);
            save(ReportCheckStyle);
        }
        return ReportCheckStyle;
    }

    public ReportCheckStyle save(ReportCheckStyle ReportCheckStyle){
        return ReportCheckStyleRepository.save(ReportCheckStyle);
    }

    public void create(ReportCheckStyle ReportCheckStyleToEntity) {
    }
}
