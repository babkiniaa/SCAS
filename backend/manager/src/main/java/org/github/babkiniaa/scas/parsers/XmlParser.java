package org.github.babkiniaa.scas.parsers;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;

public abstract class XmlParser implements Parser {
    String checkAttribute(StartElement startElement, String attributeName, String pref) {
        String report = "";
        if (startElement.getAttributeByName(new QName(attributeName)) != null) {
            String attribute = startElement.getAttributeByName(new QName(attributeName)).toString();
            report += pref + attribute.substring(attribute.indexOf('=') + 1);
        }
        return report;
    }

    public String parse(String path) {
        return null;
    }

}
