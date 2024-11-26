package org.github.babkiniaa.scas.utils.analysis;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.FindBugs;
import edu.umd.cs.findbugs.FindBugs2;
import edu.umd.cs.findbugs.TextUICommandLine;
import org.github.babkiniaa.scas.dto.reportsDto.BugInstanceCustomDto;
import org.github.babkiniaa.scas.reporters.MyBg;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinAnalysis {

    public static List<BugInstanceCustomDto> spotbugs(String dir) throws IOException {
        String bugOut = dir + "\\Bugs.spot";
        FindBugs2 findBugs2 = new FindBugs2();
        String[] arg = new String[1];
        arg[0] = dir;

        FindBugs.processCommandLine(new TextUICommandLine(), arg, findBugs2);
        findBugs2.setBugReporter(new MyBg(findBugs2.getBugReporter(), bugOut));
        FindBugs.runMain(findBugs2, new TextUICommandLine());
        ObjectInputStream bugInputStream = new ObjectInputStream(new FileInputStream(bugOut));
        List<BugInstanceCustomDto> bugs = new ArrayList<>();

        while(true) {
            try {
                bugs.add((BugInstanceCustomDto) bugInputStream.readObject());
            } catch (EOFException | ClassNotFoundException e) {
                bugInputStream.close();
                break;
            }
        }

        return bugs;
    }

}