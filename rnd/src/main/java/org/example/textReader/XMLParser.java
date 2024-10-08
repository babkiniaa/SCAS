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

public class XMLParser {

    String parseSpotBugs(String path) throws XMLStreamException, IOException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));
        String report = "";
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
//            System.out.println(nextEvent);
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "ShortMessage":
                    case "Message":
                    case "LongMessage":
                        report += '\n';
                        report += reader.nextEvent().asCharacters().getData();
                        break;
                    case "BugInstance":
                        if (startElement.getAttributeByName(new QName("cweid")) == null){
                            report += "\nBug id in CWE: No";
                        } else {
                            String cwe = startElement.getAttributeByName(new QName("cweid")).toString();
                            report += "\nBug id in CWE: " + cwe.substring(7, cwe.length() - 1) ;
                        }
                        break;
                    case "SourceLine":
                        String srcF = startElement.getAttributeByName(new QName("sourcefile")).toString();
                        report += "\nBug from file: " + srcF.substring(srcF.indexOf('=') + 1);
                        String srcC = startElement.getAttributeByName(new QName("classname")).toString();
                        report += "\nBug from class: " + srcC.substring(srcC.indexOf('=') + 1);
                        String srcD = startElement.getAttributeByName(new QName("sourcepath")).toString();
                        report += "\nWith path: " + srcD.substring(srcD.indexOf('=') + 1);
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
            }
        }

        return report;
    }

    String parsePMD(String path) throws XMLStreamException, IOException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));
        String report = "";
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
//            System.out.println(nextEvent);
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "file":
                        if (startElement.getAttributeByName(new QName("name")) != null){
                            String name = startElement.getAttributeByName(new QName("name")).toString();
                            report += "\nFile: " + name.substring(name.indexOf('=') + 1);
                        }
                        break;
                    case "violation":
                        if (startElement.getAttributeByName(new QName("beginline")) != null){
                            String begLine = startElement.getAttributeByName(new QName("beginline")).toString();
                            report += "\nBegining in line: " + begLine.substring(begLine.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("begincolumn")) != null){
                            String begCol = startElement.getAttributeByName(new QName("begincolumn")).toString();
                            report += "\nBegining in column: " + begCol.substring(begCol.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("endline")) != null){
                            String endLine = startElement.getAttributeByName(new QName("endline")).toString();
                            report += "\nEnding in line: " + endLine.substring(endLine.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("endcolumn")) != null){
                            String endCol = startElement.getAttributeByName(new QName("endcolumn")).toString();
                            report += "\nEnding in column: " + endCol.substring(endCol.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("rule")) != null){
                            String rule = startElement.getAttributeByName(new QName("rule")).toString();
                            report += "\nRule is " + rule.substring(rule.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("ruleset")) != null){
                            String ruleset = startElement.getAttributeByName(new QName("ruleset")).toString();
                            report += "\nFrom ruleset: " + ruleset.substring(ruleset.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("class")) != null){
                            String clas = startElement.getAttributeByName(new QName("class")).toString();
                            report += "\nFor class: " + clas.substring(clas.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("package")) != null){
                            String pack = startElement.getAttributeByName(new QName("package")).toString();
                            report += "\nFrom package: " + pack.substring(pack.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("method")) != null){
                            String meth = startElement.getAttributeByName(new QName("method")).toString();
                            report += "\nFor method: " + meth.substring(meth.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("variable")) != null){
                            String var = startElement.getAttributeByName(new QName("variable")).toString();
                            report += "\nFor variable: " + var.substring(var.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("priority")) != null){
                            String prior = startElement.getAttributeByName(new QName("priority")).toString();
                            report += "\nWith prior: " + prior.substring(prior.indexOf('=') + 1);
                        }
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
            }
        }

        return report;
    }

    String parseCheck(String path) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));
        String report = "";
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "file":
                        if (startElement.getAttributeByName(new QName("name")) != null){
                            String name = startElement.getAttributeByName(new QName("name")).toString();
                            report += "\nFile: " + name.substring(name.indexOf('=') + 1);
                        }
                        break;
                    case "error":
                        if (startElement.getAttributeByName(new QName("source")) != null){
                            String src = startElement.getAttributeByName(new QName("source")).toString();
                            report += "\nError: " + src.substring(src.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("message")) != null){
                            String mess = startElement.getAttributeByName(new QName("message")).toString();
                            report += "\nExplanation: " + mess.substring(mess.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("line")) != null){
                            String line = startElement.getAttributeByName(new QName("line")).toString();
                            report += "\nBegining in line: " + line.substring(line.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("column")) != null){
                            String col = startElement.getAttributeByName(new QName("column")).toString();
                            report += "\nBegining in column: " + col.substring(col.indexOf('=') + 1);
                        }
                        if (startElement.getAttributeByName(new QName("severity")) != null){
                            String sever = startElement.getAttributeByName(new QName("severity")).toString();
                            report += "\nSeverity is " + sever.substring(sever.indexOf('=') + 1);
                        }
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
            }
        }

        return report;

    }

}
