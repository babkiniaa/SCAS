package org.github.babkiniaa.scas.utils;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class BinAnalysis {
    public void spotbugs(String dir) throws IOException, XMLStreamException {

//        System.setProperty("maven.home", "C:\\Program Files\\maven");
        System.setProperty("maven.home", "C:\\apache-maven-3.9.0");

        InvocationRequest request = new DefaultInvocationRequest();
//        request.setPomFile(new File("C:\\Users\\Ярик\\Desktop\\sast\\pom.xml"));
        request.setPomFile(new File(System.getProperty("user.dir") + "\\pom.xml"));

        String ddist = "-Ddist=" + dir + " spotbugs:check";
        request.setGoals(Collections.singletonList(ddist));

        Invoker invoker = new DefaultInvoker();
        try {
            invoker.execute(request);
            System.out.println("Maven command executed successfully!");
        } catch (Exception e) {
            System.err.println("Error executing Maven command: " + e.getMessage());
            e.printStackTrace();
        }

        Writer writer = new Writer();
        XMLParser xmlParser = new XMLParser();
//        System.out.println(xmlParser.parse("C:\\sber\\SberTasks2024\\target\\spotbugsXml.xml"));
//        writer.writeFile(xmlParser.parse("C:\\Users\\Ярик\\Desktop\\sast\\target\\spotbugsXml.xml"));
        //writer.writeFile(xmlParser.parse(System.getProperty("user.dir") + "\\target\\spotbugsXml.xml"));
        System.out.println("looooop");
    }
}
