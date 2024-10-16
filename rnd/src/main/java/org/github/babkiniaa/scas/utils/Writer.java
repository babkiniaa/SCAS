package org.github.babkiniaa.scas.utils;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Writer {
    public void createLog() {
        File log = new File("log");
        if (!log.exists()) {
            log.mkdir();
        }
    }

    public void writeFile(String str) {
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH_mmss"));
        String fileName = "log/log_" + timeStamp + ".txt";
        cleanLogDirectory();
        try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
            writer.println(str);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void writeFile(List<String> strs) {
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH_mmss"));
        String fileName = "log/log_" + timeStamp + ".txt";
        cleanLogDirectory();
        try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
            for (String str : strs) {
                    writer.println(str);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void cleanLogDirectory() {
        File logDirectory = new File("log");
        File[] files = logDirectory.listFiles();
        if (files != null && files.length > 1) {
            Arrays.stream(files)
                    .sorted((f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()))
                    .skip(1)
                    .forEach(File::delete);
        }
    }

}
