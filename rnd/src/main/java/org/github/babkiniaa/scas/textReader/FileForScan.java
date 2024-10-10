package org.github.babkiniaa.scas.textReader;

import lombok.*;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileForScan {
    private String cloneDirectoryPath = "";
    private String cloneDirectoryPathDeskstop = "";

    public void searchSubDirectory() throws IOException {
        java.io.File dir = new java.io.File(cloneDirectoryPath);
        showFiles(dir.listFiles());
    }

    public void showFiles(java.io.File[] files) throws IOException {
        for (java.io.File file : files) {
            if (file.getName().charAt(0) != '.') {
                if (file.isDirectory()) {
                    showFiles(file.listFiles()); // Calls same method again.
                } else {
                    System.out.println(file.getName());
                    System.out.println(Files.readAllLines(Path.of(file.getAbsolutePath())));
                    System.out.println("-------------------------------------------------------------------------------------------------------------");
                }

            }
        }
    }

    public void del() throws IOException {
        File f = new File(cloneDirectoryPath);
        File fDesktop = new File(cloneDirectoryPathDeskstop);
        if(f.exists()) {
            deleteDir(f);
        }
        if(fDesktop.exists()) {
            deleteDir(fDesktop);
        }

//        deleteDir(f);
//        FileUtils.deleteDirectory(fDesktop);
//        if(f.exists()) {
//            ProcessBuilder processBuilder = new ProcessBuilder();
//
//            String dirdel = "Remove-Item -Recurse -Force " + cloneDirectoryPath;
//            processBuilder.command(
//                    "powershell.exe",
//                    "-Command",
//                    dirdel
//            );
//            try {
//                // Запускаем процесс
//                Process process = processBuilder.start();
//                f.delete();
//
//                // Читаем стандартный вывод
//                BufferedReader reader =
//                        new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                }
//
//                // Ожидаем завершения процесса и получаем его результат
//                int exitCode = process.waitFor();
//                System.out.println("\nКоманда завершена с кодом: " + exitCode);
//
//            } catch (InterruptedException | IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if(fDesktop.exists()) {
//            ProcessBuilder processBuilder = new ProcessBuilder();
//
//            String dirdel = "Remove-Item -Recurse -Force " + cloneDirectoryPathDeskstop;
//            processBuilder.command(
//                    "powershell.exe",
//                    "-Command",
//                    dirdel
//            );
//            try {
//                // Запускаем процесс
//                Process process = processBuilder.start();
//                f.delete();
//
//                // Читаем стандартный вывод
//                BufferedReader reader =
//                        new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                }
//
//                // Ожидаем завершения процесса и получаем его результат
//                int exitCode = process.waitFor();
//                System.out.println("\nКоманда завершена с кодом: " + exitCode);
//
//            } catch (InterruptedException | IOException e) {
//                e.printStackTrace();
//            }
//        }

        System.out.println("Directory and its contents deleted successfully.");
    }

    void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }
}
