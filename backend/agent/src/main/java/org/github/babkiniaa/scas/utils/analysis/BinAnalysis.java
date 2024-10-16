package org.github.babkiniaa.scas.utils.analysis;

import org.apache.maven.shared.invoker.*;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.util.Collections;

@Component
public class BinAnalysis {

    public static void spotbugs(String dir) throws XMLStreamException, MavenInvocationException {
        System.setProperty("maven.home", System.getenv("M2_HOME"));
        InvocationRequest request = new DefaultInvocationRequest();
        String ddist = "-Ddist=" + dir + " -Dspotout=" + dir.split("/")[dir.split("/").length - 1] + " spotbugs:check";
        request.setPomFile(new File(System.getProperty("user.dir") + "\\backend\\agent\\pom.xml"));
        request.setGoals(Collections.singletonList(ddist));
        Invoker invoker = new DefaultInvoker();

        try {
            invoker.execute(request);
        } catch (Exception e) {
            throw e;
        }
    }
}