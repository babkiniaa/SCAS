package org.github.babkiniaa.scas.Mapper.Custom;

import edu.umd.cs.findbugs.BugInstance;
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
        bugInstanceCustomDto.setInstanceOccurrenceNum(violation.getInstanceOccurrenceNum());
        bugInstanceCustomDto.setInstanceOccurrenceMax(violation.getInstanceOccurrenceMax());
        bugInstanceCustomDto.setFirstVersion(violation.getFirstVersion());
        bugInstanceCustomDto.setLastVersion(violation.getLastVersion());
        bugInstanceCustomDto.setIntroducedByChangeOfExistingClass(violation.isIntroducedByChangeOfExistingClass());
        bugInstanceCustomDto.setRemovedByChangeOfPersistingClass(violation.isRemovedByChangeOfPersistingClass());

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
