package org.example.textReader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

    static List<String> parse(String path) throws XMLStreamException, IOException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));
        List<String> report = new ArrayList<String>();
//        HttpClient httpClient = HttpClient.newHttpClient();
//        String urlStr = "https://cwe-api.mitre.org/api/v1/cwe/weakness/";
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
//            System.out.println(nextEvent);
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "ShortMessage":
                    case "Message":
                    case "LongMessage":
                        report.add(reader.nextEvent().asCharacters().getData());
                        break;
                    case "BugInstance":
                        if (startElement.getAttributeByName(new QName("cweid")) == null){
                            report.add("\nBug id in CWE: No");
                        } else {
                            String cwe = startElement.getAttributeByName(new QName("cweid")).toString();
                            report.add("\nBug id in CWE: " + cwe.substring(7, cwe.length() - 1));
//                            URL url = new URL(urlStr + cwe.substring(7, cwe.length() - 1));
//                            HttpResponse response
                        }
                        break;
                    case "SourceLine":
                        String srcF = startElement.getAttributeByName(new QName("sourcefile")).toString();
                        report.add("Bug from file: " + srcF);
                        String srcC = startElement.getAttributeByName(new QName("classname")).toString();
                        report.add("Bug from class: " + srcC);
                        String srcD = startElement.getAttributeByName(new QName("sourcepath")).toString();
                        report.add("With path: " + srcD);
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
            }
        }

        return report;
    }
}
