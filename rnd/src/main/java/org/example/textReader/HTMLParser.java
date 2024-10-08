package org.example.textReader;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {

    public static String getAllMatches(String text, String regex) {
        String matches = "";
        Matcher m = Pattern.compile("(?=(" + regex + "))").matcher(text);
        while(m.find()) {
            matches += m.group(1);
        }
        return matches;
    }

    String parseDep(String path) throws IOException {

        Document doc = Jsoup.parse(new File(path));
        String report = getAllMatches(doc.body().text(), "Project/Scope:.*\n");

        return report;
    }

}
