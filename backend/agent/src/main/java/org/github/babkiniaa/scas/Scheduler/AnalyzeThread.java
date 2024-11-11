package org.github.babkiniaa.scas.Scheduler;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.github.babkiniaa.scas.dto.Response.Report;
import org.github.babkiniaa.scas.dto.typeForMap.MethodAndTypeAnalysis;
import org.github.babkiniaa.scas.utils.DeleteFileUtil;
import org.github.babkiniaa.scas.utils.GitUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class AnalyzeThread extends Thread{

    private String url;

    private List<String> analysis;

    private final HashMap<String, MethodAndTypeAnalysis> methodMap = new HashMap<>();

    {
        methodMap.put("PMD", new MethodAndTypeAnalysis(this::reportPmd, "Static"));
        methodMap.put("CheckStyle", new MethodAndTypeAnalysis(this::reportCheckstyle, "Static"));
        methodMap.put("SpotBugs", new MethodAndTypeAnalysis(this::reportSpotBugs, "Binary"));
        methodMap.put("OWASP", new MethodAndTypeAnalysis(this::reportOwasp, "Binary"));
    }

    @Override
    public void run(){
        String dir = System.getProperty("user.dir") + "/down";
        Report report;
        try {
            GitUtil.cloneRepository(url, dir);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        for (String run : analysis) {
            report = (Report) methodMap.get(run).getFunction().apply(dir);
        }
        DeleteFileUtil.deleteDir(new File(dir));
    }

    private Report reportOwasp(String dir) {
        return null;
    }

    private Report reportSpotBugs(String dir) {
        return null;
    }

    private Report reportCheckstyle(String dir) {
        return null;
    }

    private Report reportPmd(String dir) {
        return null;
    }

}
