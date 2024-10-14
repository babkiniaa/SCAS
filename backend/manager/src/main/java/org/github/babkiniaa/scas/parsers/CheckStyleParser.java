package org.github.babkiniaa.scas.parsers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CheckStyleParser extends XmlParser {
    @Override
    String checkAttribute(StartElement startElement, String attributeName, String pref) {
        return super.checkAttribute(startElement, attributeName, pref);
    }

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
                        report += checkAttribute(startElement, "name", "\nFile: ");
                        break;
                    case "error":
                        report += checkAttribute(startElement, "source", "\nError: ");
                        report += checkAttribute(startElement, "message", "\nExplanation: ");
                        report += checkAttribute(startElement, "line", "\nBegining in line: ");
                        report += checkAttribute(startElement, "column", "\nBegining in column: ");
                        report += checkAttribute(startElement, "severity", "\nSeverity is ");
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
            }
        }

        return report;


    }
}
