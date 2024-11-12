package org.github.babkiniaa.scas.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RegisterTaskDto {

   private String url;

   private List<String> needReports;

   private long projectId;
}
