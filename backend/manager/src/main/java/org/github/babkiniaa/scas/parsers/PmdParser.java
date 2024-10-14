package org.github.babkiniaa.scas.parsers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PmdParser extends XmlParser {
    @Override
    String checkAttribute(StartElement startElement, String attributeName, String pref) {
        return super.checkAttribute(startElement, attributeName, pref);
    }

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
                    case "violation":
                        report += checkAttribute(startElement, "beginline", "\nBegining in line: ");
                        report += checkAttribute(startElement, "begincolumn", "\nBegining in column: ");
                        report += checkAttribute(startElement, "endline", "\nEnding in line: ");
                        report += checkAttribute(startElement, "endcolumn", "\nEnding in column: ");
                        report += checkAttribute(startElement, "rule", "\nRule is ");
                        report += checkAttribute(startElement, "ruleset", "\nFrom ruleset: ");
                        report += checkAttribute(startElement, "class", "\nFor class: ");
                        report += checkAttribute(startElement, "package", "\nFrom package: ");
                        report += checkAttribute(startElement, "method", "\nFor method: ");
                        report += checkAttribute(startElement, "variable", "\nFor variable: ");
                        report += checkAttribute(startElement, "priority", "\nWith prior: ");
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
            }
        }

        return report;
    }
}
