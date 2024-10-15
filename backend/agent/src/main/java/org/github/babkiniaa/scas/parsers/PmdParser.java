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
public class PmdParser extends XmlParser {
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
                        report += super.checkAttribute(startElement, "name", "\nFile: ");
                        break;
                    case "violation":
                        report += super.checkAttribute(startElement, "beginline", "\nBegining in line: ");
                        report += super.checkAttribute(startElement, "begincolumn", "\nBegining in column: ");
                        report += super.checkAttribute(startElement, "endline", "\nEnding in line: ");
                        report += super.checkAttribute(startElement, "endcolumn", "\nEnding in column: ");
                        report += super.checkAttribute(startElement, "rule", "\nRule is ");
                        report += super.checkAttribute(startElement, "ruleset", "\nFrom ruleset: ");
                        report += super.checkAttribute(startElement, "class", "\nFor class: ");
                        report += super.checkAttribute(startElement, "package", "\nFrom package: ");
                        report += super.checkAttribute(startElement, "method", "\nFor method: ");
                        report += super.checkAttribute(startElement, "variable", "\nFor variable: ");
                        report += super.checkAttribute(startElement, "priority", "\nWith prior: ");
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
            }
        }

        return report;
    }
}