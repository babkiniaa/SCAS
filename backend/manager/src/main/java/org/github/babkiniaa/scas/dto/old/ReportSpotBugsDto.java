package org.github.babkiniaa.scas.dto.old;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.reportsDto.BugInstanceCustomDto;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportSpotBugsDto {

    private int id;

    private String hash;

    private List<BugInstanceCustomDto> reportList;


}
