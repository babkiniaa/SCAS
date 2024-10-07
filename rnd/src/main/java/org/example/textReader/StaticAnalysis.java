package org.example.textReader;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@NoArgsConstructor
@AllArgsConstructor
public class StaticAnalysis {

    private String nameFile = " ";

    public void startOWASP(String scanDir) throws IOException {
        try {

            ProcessBuilder processBuilder = new ProcessBuilder();
            String dirReport = "-DdistOWASP=" + nameFile;
            // Установка директории, в которую нужно перейти
            processBuilder.directory(new File(scanDir));

            // Пример команды, которую вы хотите выполнить в новой директории
            processBuilder.command(
//                    "C:\\Program Files\\maven\\bin\\mvn.cmd",
                    "C:\\apache-maven-3.9.0\\bin\\mvn.cmd",
//                    dirReport,
                    "org.owasp:dependency-check-maven:check"
            );

            Process process = processBuilder.start();
            process.destroy();



            // Обработка вывода или ошибок процесса, если необходимо

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void startPmd() throws Exception {
        // Указываем команду и аргументы
        ProcessBuilder processBuilder = new ProcessBuilder();
        String dirReport = "-DdistPMD=" + nameFile;
        processBuilder.command(
//                "C:\\Program Files\\maven\\bin\\mvn.cmd",
                "C:\\apache-maven-3.9.0\\bin\\mvn.cmd",
                dirReport,
                "pmd:pmd"
        );
        try {
            // Запускаем процесс
            Process process = processBuilder.start();

            // Читаем стандартный вывод
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));


            // Ожидаем завершения процесса и получаем его результат
            int exitCode = process.waitFor();
            process.destroy();
            System.out.println("\nКоманда завершена с кодом: " + exitCode);

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void startCheckStyle() throws Exception {
        // Указываем команду и аргументы
        ProcessBuilder processBuilder = new ProcessBuilder();
        String dirReport = "-DdistCheckerStyle=" + nameFile;
        processBuilder.command(
//                "C:\\Program Files\\maven\\bin\\mvn.cmd",
                "C:\\apache-maven-3.9.0\\bin\\mvn.cmd",
                dirReport,
                "checkstyle:checkstyle"
        );
        try {
            // Запускаем процесс
            Process process = processBuilder.start();

            // Читаем стандартный вывод
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));


            // Ожидаем завершения процесса и получаем его результат
            int exitCode = process.waitFor();
            process.destroy();
            System.out.println("\nКоманда завершена с кодом: " + exitCode);

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
