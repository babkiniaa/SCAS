package org.example.textReader;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Manager {

    private String url = " ";

    public void start() throws IOException {
        BinAnalysis binAnalysis = new BinAnalysis();
        String currentDir = System.getProperty("user.dir") + "/src/main/java/projectDownload1";
        String currentDirDesktop = "C:/Users/Ярик/Desktop/down/Download1";
        currentDir = freePath(0, currentDir);

//        FileForScan fileForScan = new FileForScan(currentDir, currentDirDesktop);
//        fileForScan.del();
//        url = "https://github.com/ShchekoturovDA/SberTasks2024.git";
//        String url = "https://github.com/babkiniaa/STASIK.git";

        GitStatus gitStatus = new GitStatus(url, currentDir);
        gitStatus.cloneRepository();
        currentDirDesktop = freePath(0, currentDirDesktop);
        GitStatus gitStatus1 = new GitStatus(url, currentDirDesktop);
        gitStatus1.cloneRepository();

        StaticAnalysis staticAnalysis = new StaticAnalysis(currentDirDesktop.substring(27, currentDirDesktop.length()));
        staticAnalysis.startOWASP(currentDirDesktop);
        staticAnalysis.startPmd();
        staticAnalysis.startCheckStyle();
//        fileForScan.del();

//        БИН анализ
//        try {
//            binAnalysis.sportbugs();
//        } catch (XMLStreamException e) {
//            e.printStackTrace();
//        }


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
