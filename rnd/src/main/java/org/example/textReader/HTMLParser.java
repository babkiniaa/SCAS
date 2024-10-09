package org.example.textReader;


import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Objects;
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

    File[] findAll(File[] files){
        File[] ret = null;
        for (File file : files){
            if (file.isDirectory()){
                ret = ArrayUtils.addAll(ret, findAll(file.listFiles()));
                ret = ArrayUtils.addAll(ret, file.listFiles((FileFilter) new WildcardFileFilter("dependency-check-report.html")));
            }
        }
        return ret;
    }

    String parseDep(String path) throws IOException {
        String report = "";
        File dir = new File(path);
        FileFilter fileFilter = new WildcardFileFilter("dependency-check-report.html");
        File[] files = findAll(Objects.requireNonNull(dir.listFiles()));
        for (File file : files) {
            Document doc = Jsoup.parse(file);
            report += getAllMatches(doc.body().text(), "Project/Scope:.*\n");
            report += "\n";
        }

        return report;
    }

}
