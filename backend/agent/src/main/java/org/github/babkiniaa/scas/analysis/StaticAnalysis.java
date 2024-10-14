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

    public void startOWASP(String scanDir) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(scanDir));
        processBuilder.command(
                System.getenv("M2_HOME") + "\\bin\\mvn.cmd",
                "org.owasp:dependency-check-maven:check"
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