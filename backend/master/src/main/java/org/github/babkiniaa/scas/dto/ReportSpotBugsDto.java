package org.github.babkiniaa.scas.dto;

import edu.umd.cs.findbugs.BugInstance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportSpotBugsDto {

    private int id;

    private String hash;

    private List<BugInstance> reportList;


}
