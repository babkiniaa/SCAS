package org.example.textReader;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.maven.Maven;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Manager {

    private String url = " ";
    private String repOWASP = " ";
    private String repPMD = " ";
    private String repStyle = " ";
    private String repSpotBug = " ";

    public void start() throws IOException {
        BinAnalysis binAnalysis = new BinAnalysis();
        String currentDir = System.getProperty("user.dir") + "/src/main/java/projectDownload1";
//        String currentDirDesktop = "C:/Users/Ярик/Desktop/down/Download1";
        String currentDirDesktop = "C:/Users/Дмитрий/Desktop/down/Download1";
        currentDir = freePath(0, currentDir);

//        fileForScan.del();
//        url = "https://github.com/ShchekoturovDA/SberTasks2024.git";
//        String url = "https://github.com/babkiniaa/STASIK.git";

        GitStatus gitStatus = new GitStatus(url, currentDir);
        gitStatus.cloneRepository();
        currentDirDesktop = freePath(0, currentDirDesktop);
        GitStatus gitStatus1 = new GitStatus(url, currentDirDesktop);
        gitStatus1.cloneRepository();

        FileForScan fileForScan = new FileForScan(currentDir, currentDirDesktop);
        StaticAnalysis staticAnalysis = new StaticAnalysis(currentDirDesktop.substring(27, currentDirDesktop.length()));

//
//        System.setProperty("maven.home", "C:\\Program Files\\maven");
        System.setProperty("maven.home", "C:\\apache-maven-3.9.0");

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(currentDirDesktop + "\\pom.xml"));

        request.setGoals(Collections.singletonList("compile"));

        Invoker invoker = new DefaultInvoker();
        try {
            invoker.execute(request);
            System.out.println("Maven command executed successfully!");
        } catch (Exception e) {
            System.err.println("Error executing Maven command: " + e.getMessage());
            e.printStackTrace();
        }
//

        staticAnalysis.startOWASP(currentDirDesktop);
        staticAnalysis.startPmd();
        staticAnalysis.startCheckStyle();

//        БИН анализ
        try {
            binAnalysis.spotbugs(currentDirDesktop);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        System.out.println(" The end ");

        fileForScan.del();
    }

    public String freePath(int count, String baseDirectoryPath) {
        File file = new File(baseDirectoryPath);
        String modifiedPath = baseDirectoryPath;

        while (file.exists()) {
            modifiedPath = baseDirectoryPath + "_" + count;
            file = new File(modifiedPath);
            count++;
        }

        return modifiedPath;
    }

}
