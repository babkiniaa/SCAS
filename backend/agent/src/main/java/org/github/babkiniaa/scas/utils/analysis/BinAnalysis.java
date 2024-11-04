package org.github.babkiniaa.scas.utils.analysis;

import edu.umd.cs.findbugs.FindBugs;
import edu.umd.cs.findbugs.FindBugs2;
import edu.umd.cs.findbugs.TextUICommandLine;
import org.github.babkiniaa.scas.reporters.MyBg;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class BinAnalysis {

    public static void spotbugs(String dir) throws IOException {
//        System.setProperty("maven.home", System.getenv("M2_HOME"));
//        InvocationRequest request = new DefaultInvocationRequest();
//        Invoker invoker = new DefaultInvoker();
//        String ddist = "-Ddist=" + dir + " -Dspotout=" + dir.split("/")[dir.split("/").length - 1] + " spotbugs:check";
//        request.setPomFile(new File(System.getProperty("user.dir") + "\\backend\\agent\\pom.xml"));
//        request.setGoals(Collections.singletonList(ddist));
//
//        try {
//            invoker.execute(request);
//        } catch (Exception e) {
//            throw e;
//        }

        FindBugs2 findBugs2 = new FindBugs2();
        String[] arg = new String[1];
        arg[0] = dir;
        FindBugs.processCommandLine(new TextUICommandLine(), arg, findBugs2);
        findBugs2.setBugReporter(new MyBg(findBugs2.getBugReporter()));
        FindBugs.runMain(findBugs2, new TextUICommandLine());
    }

}