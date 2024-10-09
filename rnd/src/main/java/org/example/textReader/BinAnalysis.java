package org.example.textReader;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.util.Collections;

public class BinAnalysis {
    public void spotbugs(String dir) throws XMLStreamException {
        System.setProperty("maven.home", System.getenv("M2_HOME"));
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(System.getProperty("user.dir") + "\\pom.xml"));
        String ddist = "-Ddist=" + dir + " -Dspotout=" + dir.split("/")[dir.split("/").length - 1] +  " spotbugs:check";
        request.setGoals(Collections.singletonList(ddist));
        Invoker invoker = new DefaultInvoker();
        try {
            invoker.execute(request);
            System.out.println("Maven command executed successfully!");
        } catch (Exception e) {
            System.err.println("Error executing Maven command: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
