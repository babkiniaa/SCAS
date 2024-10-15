package org.github.babkiniaa.scas.parsers;

import org.springframework.stereotype.Component;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Component
public class CheckStyleParser extends XmlParser {
    @Override
    public String parse(String path) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = null;
        try {
            reader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));
        } catch (XMLStreamException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String report = "";
        while (reader.hasNext()) {
            XMLEvent nextEvent = null;
            try {
                nextEvent = reader.nextEvent();
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "file":
                        String fileName = super.checkAttribute(startElement, "name", "\nFile: ")
                                .substring((System.getProperty("user.dir") + "/backend/agent/src/main/java/").length());
                        if (!fileName.split("\\\\")[0].equals("org")){
                            report += fileName;
                        } else {
                            startElement = nextEvent.asStartElement();
                        }
                        break;
                    case "error":
                        report += super.checkAttribute(startElement, "source", "\nError: ");
                        report += super.checkAttribute(startElement, "message", "\nExplanation: ");
                        report += super.checkAttribute(startElement, "line", "\nBegining in line: ");
                        report += super.checkAttribute(startElement, "column", "\nBegining in column: ");
                        report += super.checkAttribute(startElement, "severity", "\nSeverity is ");
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
            }
        }

        return report;


    }
}
