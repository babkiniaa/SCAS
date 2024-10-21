package org.github.babkiniaa.scas.parsers;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DependencyCheckParser implements Parser {

    public static String getAllMatches(String text, String regex) {
        String matches = "";
        Matcher m = Pattern.compile("(?=(" + regex + "))").matcher(text);

        while (m.find()) {
            matches += m.group(1);
        }

        return matches;
    }

    File[] findAll(File[] files) {
        File[] ret = null;

        for (File file : files) {
            if (file.isDirectory()) {
                ret = ArrayUtils.addAll(ret, findAll(file.listFiles()));
                ret = ArrayUtils.addAll(ret, file.listFiles((FileFilter) new WildcardFileFilter("dependency-check-report.html")));
            }
        }

        return ret;
    }

    @Override
    public String parse(String path) {
        String report = "";
        File dir = new File(path);
        FileFilter fileFilter = new WildcardFileFilter("dependency-check-report.html");
        File[] files = findAll(dir.listFiles());

        for (File file : files) {
            Document doc = null;
            try {
                doc = Jsoup.parse(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            report += getAllMatches(doc.body().text(), "Project/Scope:.*\n");
            report += "\n";
        }

        return report;
    }
}