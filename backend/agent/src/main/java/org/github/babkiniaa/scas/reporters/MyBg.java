package org.github.babkiniaa.scas.reporters;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.DelegatingBugReporter;
import org.github.babkiniaa.scas.mapper.ReportSpotBugsMapper;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyBg extends DelegatingBugReporter {

    ObjectOutputStream objectOutputStream;

    public MyBg(BugReporter delegate, String out) throws IOException {
        super(delegate);
        objectOutputStream = new ObjectOutputStream(new FileOutputStream(out));
    }

    public void reportBug(@Nonnull BugInstance bugInstance) {
        System.out.println("Added " + bugInstance);
        try {
            objectOutputStream.writeObject(ReportSpotBugsMapper.bugInstanceToBugInstanceCustom(bugInstance));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void finish(){
        System.out.println("GETOUT!!!!!!!");
        try {
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
