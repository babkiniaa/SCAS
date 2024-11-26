package org.github.babkiniaa.scas.reporters;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.reportsDto.ViolationCustomDto;
import org.github.babkiniaa.scas.mapper.ReportCheckStyleMapper;
import org.github.babkiniaa.scas.parsers.CheckStyleParser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

@Setter
@Getter
@AllArgsConstructor
public class MyList implements AuditListener {

    ObjectOutputStream objectOutputStream;

    @Override
    public void auditStarted(AuditEvent auditEvent) {
    }

    @Override
    public void auditFinished(AuditEvent auditEvent) {
        try {
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fileStarted(AuditEvent auditEvent) {

    }

    @Override
    public void fileFinished(AuditEvent auditEvent) {

    }

    @Override
    public void addError(AuditEvent auditEvent) {
        try {
            ViolationCustomDto violationCustomDto = ReportCheckStyleMapper.checkStyleToCheckstyleCustom(auditEvent.getViolation());
            violationCustomDto.setCustomMessage(auditEvent.getMessage());
            String[] cats = auditEvent.getFileName().split("\\\\");
            violationCustomDto.setSource(cats[cats.length - 1]);
            objectOutputStream.writeObject(violationCustomDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addException(AuditEvent auditEvent, Throwable throwable) {

    }
}
