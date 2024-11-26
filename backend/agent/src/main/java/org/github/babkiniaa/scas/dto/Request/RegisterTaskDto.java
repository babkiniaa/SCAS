package org.github.babkiniaa.scas.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Setter
@Getter
public class RegisterTaskDto {

   private String url;

   private List<String> needReports;

   private long idProject;

   private String branch;

   private String commit;

   private HashMap<String, List<String>> hashAndAnalyze;
}
