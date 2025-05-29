package org.github.babkiniaa.scas.mapper.deadCode;

import org.github.babkiniaa.scas.dto.oldReports.OWASPVulnerabilitiesDTO;
import org.github.babkiniaa.scas.dto.reportsDto.DeadCodeDto.SummaryCustomDto;

import org.github.babkiniaa.scas.entity.reportsEntity.DeadCodeEntity.SummaryCustom;
import org.shchek.exps.CodeBlock;
import org.shchek.exps.Module;
import org.shchek.exps.Node;
import org.shchek.exps.Summary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SummariesMapper {

    public SummaryCustomDto sumToDTO(Summary summary) {
        SummaryCustomDto summaryDto = new SummaryCustomDto();

        summaryDto.setMethodOrField(summary.getMethodOrField());
        summaryDto.setResult(summary.getResult());
        List<String> deadStr = new ArrayList<>();
        for(Node n : summary.getDeadNodes()){
            deadStr.add(n.toString());
            for( CodeBlock cb: n.getCodeSector()){
                deadStr.add(cb.toString());
            }
        }
        summaryDto.setDeadNodes(deadStr);
        summaryDto.setBefore(summary.getBefore());
        summaryDto.setAfter(summary.getAfter());
        List<String> modStr = new ArrayList<>();
        List<Double> useStr = new ArrayList<>();
        for(Module m : summary.getModules()){
            modStr.add(m.getName());
            useStr.add(m.getUsage());
        }
        summaryDto.setModuleNames(modStr);
        summaryDto.setModuleUsage(useStr);
        return summaryDto;
    }

    public List<SummaryCustomDto> sumstoDTO(List<Summary> summaries) {

        List<SummaryCustomDto> sumDTOs = new ArrayList<>();

        for (Summary s : summaries) {
            sumDTOs.add(sumToDTO(s));
        }

        return sumDTOs;
    }

}
