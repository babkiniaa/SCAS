package org.github.babkiniaa.scas.Mapper.Custom;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.SourceLineAnnotation;
import org.github.babkiniaa.scas.dto.reportsDto.BugInstanceCustomDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class ReportSpotBugsMapper {

    public static BugInstanceCustomDto bugInstanceToBugInstanceCustom(BugInstance violation) {
        if (violation == null) {
            return null;
        }

        BugInstanceCustomDto bugInstanceCustomDto = new BugInstanceCustomDto();

        bugInstanceCustomDto.setType(violation.getType());
        bugInstanceCustomDto.setPriority(violation.getPriority());
        bugInstanceCustomDto.setInstanceHash(violation.getInstanceHash());
        bugInstanceCustomDto.setCategory(violation.getBugPattern().getCategory());
        bugInstanceCustomDto.setSource(violation.getPrimarySourceLineAnnotation().toString());
        bugInstanceCustomDto.setMessage(violation.getBugPattern().getShortDescription());

        return bugInstanceCustomDto;
    }

    public static List<BugInstanceCustomDto> bugInstanceToBugInstanceCustomList(List<BugInstance> reports) {
        if (reports == null) {
            return null;
        }

        List<BugInstanceCustomDto> list = new ArrayList<BugInstanceCustomDto>(reports.size());
        for (BugInstance bugInstance : reports) {
            list.add(bugInstanceToBugInstanceCustom(bugInstance));
        }

        return list;
    }
}
