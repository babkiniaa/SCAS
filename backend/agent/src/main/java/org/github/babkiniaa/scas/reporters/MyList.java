package org.github.babkiniaa.scas.reporters;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

public class MyList implements AuditListener {
    @Override
    public void auditStarted(AuditEvent auditEvent) {
        System.out.println("Start Scanning");

    }

    @Override
    public void auditFinished(AuditEvent auditEvent) {
        System.out.println("End scanning");
    }

    @Override
    public void fileStarted(AuditEvent auditEvent) {
        System.out.println("Scanning " + auditEvent.getFileName());
    }

    @Override
    public void fileFinished(AuditEvent auditEvent) {
        System.out.println("End Scanning " + auditEvent.getFileName());
    }

    @Override
    public void addError(AuditEvent auditEvent) {
        System.out.println("Error: " + auditEvent.getMessage());
    }

    @Override
    public void addException(AuditEvent auditEvent, Throwable throwable) {

    }
}
