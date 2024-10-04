package org.example.textReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, XMLStreamException {
        Manager manager = new Manager();
        manager.setUrl("https://github.com/ShchekoturovDA/SberTasks2024.git");
        manager.start();
//        XMLParser xmlParser = new XMLParser();
//        System.out.println(xmlParser.parseSpotBugs(System.getProperty("user.dir") + "\\target\\spotbugsXml.xml"));

    }
    

}
