package org.example.textReader;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.eclipse.jgit.api.errors.GitAPIException;
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
    private String comment = " ";
    private String currentDir = System.getProperty("user.dir") + "/rnd/src/main/java/Download1";
    private String currentDirUser = System.getProperty("user.dir") + "/down/Download1";

    public void start() throws IOException {
        BinAnalysis binAnalysis = new BinAnalysis();
        currentDir = freePath(0, currentDir);
        GitStatus gitStatus = new GitStatus(url, currentDir);
        XMLParser xmlParser = new XMLParser();
        HTMLParser htmlParser = new HTMLParser();
        try {
            gitStatus.cloneRepository();
            currentDirUser = freePath(0, currentDirUser);
            String dirUser = currentDirUser.split("/")[currentDirUser.split("/").length - 1];
            GitStatus gitStatusDesktop = new GitStatus(url, currentDirUser);
            gitStatusDesktop.cloneRepository();
            FileForScan fileForScan = new FileForScan(currentDir, currentDirUser);
            StaticAnalysis staticAnalysis = new StaticAnalysis(dirUser);
            System.setProperty("maven.home", System.getenv("M2_HOME"));

            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(new File(currentDirUser + "\\pom.xml"));

            request.setGoals(Collections.singletonList("compile"));

            Invoker invoker = new DefaultInvoker();
            try {
                invoker.execute(request);
            } catch (Exception e) {
                comment += " Ошибка при работе invoker" + e.getMessage();
            }
            try {
                staticAnalysis.startOWASP(currentDirUser);
            } catch (Exception e) {
                comment += " Ошибка при работе OWASP" + e.getMessage();
            }
            try {
                staticAnalysis.startPmd();
            } catch (Exception e) {
                comment += " Ошибка при работе PMD" + e.getMessage();;
            }
            try {
                staticAnalysis.startCheckStyle();
            } catch (Exception e) {
                comment += " Ошибка при работе CheckerStyle" + e.getMessage();
            }
            try {
                binAnalysis.spotbugs(currentDirUser);

            } catch (XMLStreamException e) {
                comment += " Ошибка при работе spotbugs" + e.getMessage();
            }
            try {
                repOWASP = htmlParser.parseDep(currentDirUser);
            } catch (IOException e){
                comment += "Ошибка в создании отчёта по dependency " + e.getMessage();
            }
            try {
                repPMD = xmlParser.parsePMD(System.getProperty("user.dir") + "\\rnd\\target\\pmd-res\\" + dirUser + "\\pmd.xml");
            } catch (XMLStreamException | IOException e ) {
                comment += "Ошибка в создании отчёта по PMD " + e.getMessage();
            }
            try {
                repStyle = xmlParser.parseCheck(System.getProperty("user.dir") +"\\rnd\\target\\checkstyle-reports\\" + dirUser + "\\checkstyle-result.xml");
            } catch (XMLStreamException | IOException e) {
                comment += "Ошибка в создании отчёта по Checkstyle " + e.getMessage();
            }
            try {
                repSpotBug = xmlParser.parseSpotBugs(System.getProperty("user.dir") +"\\rnd\\target\\spotbugs\\"+ dirUser +"\\spotbugsXml.xml");
            } catch (XMLStreamException | IOException e) {
                comment += "Ошибка в создании отчёта по spotbugs " + e.getMessage();
            }
            fileForScan.del();
        } catch (GitAPIException e) {
            comment += "Не удалось загрузить Git, возможно ссылка не корректна" + e.getMessage();
        }
        System.out.println(comment);
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