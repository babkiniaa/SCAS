package org.github.babkiniaa.scas.analysis;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@NoArgsConstructor
@AllArgsConstructor
public class StaticAnalysis {

    public void startPmd(String nameFile) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String dirReport = "-DdistPMD=" + nameFile;
        processBuilder.command(
                System.getenv("M2_HOME") + "\\bin\\mvn.cmd",
                dirReport,
                "pmd:pmd"
        );
        try {
            Process process = processBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            }
            int exitCode = process.waitFor();
            process.destroy();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
