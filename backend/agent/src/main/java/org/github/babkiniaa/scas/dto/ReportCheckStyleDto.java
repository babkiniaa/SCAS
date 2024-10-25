package org.github.babkiniaa.scas.dto;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportCheckStyleDto {

    private int id;

    private String hash;

    private List<AuditEvent> reportList;

}
