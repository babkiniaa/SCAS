package org.github.babkiniaa.scas.reporters;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.DelegatingBugReporter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MyBg extends DelegatingBugReporter {
    List<BugInstance> bugList = new ArrayList<BugInstance>();

    public MyBg(BugReporter delegate) {
        super(delegate);
    }

    public void reportBug(@Nonnull BugInstance bugInstance) {
        System.out.println("Added " + bugInstance);
        this.bugList.add(bugInstance);
    }

    public void finish(){
        System.out.println("GETOUT!!!!!!!");
        bugList = new ArrayList<BugInstance>();
    }

}
