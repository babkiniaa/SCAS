package org.github.babkiniaa.scas.parsers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SpotBugsParser extends XmlParser {
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
                    case "ShortMessage":
                    case "Message":
                    case "LongMessage":
                        report += '\n';
                        try {
                            report += reader.nextEvent().asCharacters().getData();
                        } catch (XMLStreamException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "BugInstance":
                        report += checkAttribute(startElement, "name", "\nBug id in CWE: ");
                        break;
                    case "SourceLine":
                        report += checkAttribute(startElement, "sourcefile", "\nBug from file: ");
                        report += checkAttribute(startElement, "classname", "\nBug from class: ");
                        report += checkAttribute(startElement, "sourcepath", "\nWith path: ");
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
            }
        }

        return report;
    }
}
