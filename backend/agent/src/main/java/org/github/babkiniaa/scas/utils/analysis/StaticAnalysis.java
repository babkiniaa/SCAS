package org.github.babkiniaa.scas.utils.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class StaticAnalysis {

  public static void startPmd(String nameFile) throws Exception {
    ProcessBuilder processBuilder = new ProcessBuilder();
    String dirReport = "-DdistPMD=" + nameFile;
    processBuilder.command(
            System.getenv("M2_HOME") + "\\bin\\mvn.cmd",
            dirReport,
            "pmd:pmd");

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

  public static void startOWASP(String scanDir) throws IOException, InterruptedException {
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

  public static void startCheckStyle(String nameFile) throws Exception {
    ProcessBuilder processBuilder = new ProcessBuilder();
    String dirReport = "-DdistCheckerStyle=" + nameFile;
    processBuilder.command(
            System.getenv("M2_HOME") + "\\bin\\mvn.cmd",
            dirReport,
            "checkstyle:checkstyle"
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