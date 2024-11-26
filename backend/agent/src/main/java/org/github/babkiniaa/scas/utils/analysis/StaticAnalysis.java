package org.github.babkiniaa.scas.utils.analysis;

import com.puppycrawl.tools.checkstyle.*;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Violation;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.reporting.RuleViolation;

import org.github.babkiniaa.scas.dto.reportsDto.BugInstanceCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.ViolationCustomDto;
import org.github.babkiniaa.scas.reporters.MyList;
import org.owasp.dependencycheck.Engine;
import org.owasp.dependencycheck.dependency.Dependency;
import org.owasp.dependencycheck.exception.ExceptionCollection;
import org.owasp.dependencycheck.utils.Settings;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;


public class StaticAnalysis {


    private static List<File> listFiles(File node) {
        final List<File> result = new LinkedList<>();

        if (node.canRead()) {
            if (node.isDirectory()) {
                final File[] files = node.listFiles();
                if (files != null) {
                    for (File element : files) {
                        result.addAll(listFiles(element));
                    }
                }
            }
            else if (node.isFile() && node.toString().endsWith(".java")) {
                result.add(node);
            }
        }

        return result;
    }

    public static List<RuleViolation> startPmd(String path) throws Exception {
        PMDConfiguration config = new PMDConfiguration();

        config.setDefaultLanguageVersion(LanguageRegistry.PMD.getLanguageVersionById("java", null));
        config.addRuleSet("rules.xml");

        try (PmdAnalysis pmd = PmdAnalysis.create(config)) {

            pmd.files().addDirectory(Path.of(path), true);
            List<RuleViolation> ruleViolationList = pmd.performAnalysisAndCollectReport().getViolations();

            return ruleViolationList;
        }

    }

    public static List<Dependency> startOWASP(String scanDir) {
        Settings settings = new Settings();
        settings.setString(Settings.KEYS.AUTO_UPDATE, "false");
        settings.setString(Settings.KEYS.NVD_API_KEY, "856e72cc-cfa7-4220-b12b-19317c018957");
        Engine engine = new Engine(settings);
        List<Dependency> list2 = engine.scan(scanDir);

        try {
            engine.analyzeDependencies();
        } catch (ExceptionCollection e) {
            throw new RuntimeException(e);
        }

        List<Dependency> dependencies = Arrays.stream(engine.getDependencies())
                .filter(x -> x.getVulnerabilitiesCount() != 0)
                .toList();

        return dependencies;
    }

    public static List<ViolationCustomDto> startCheckStyle(String dir) throws Exception {
        Checker checker = new Checker();
        Configuration config2 = ConfigurationLoader.loadConfiguration("checkstyle.xml",
                new PropertiesExpander(System.getProperties()), ConfigurationLoader.IgnoredModulesOptions.OMIT,
                new ThreadModeSettings(1, 1));
        ClassLoader moduleClassLoader = Checker.class.getClassLoader();
        ModuleFactory factory = new PackageObjectFactory(Checker.class.getPackage().getName(), moduleClassLoader);

        checker.setModuleFactory(factory);
        checker.configure(config2);
        String bugOut = dir + "\\Style.check";
        checker.addListener(new MyList(new ObjectOutputStream(new FileOutputStream(bugOut))));
        List<File> files = listFiles(new File(dir));
        int process = checker.process(files);

        ObjectInputStream bugInputStream = new ObjectInputStream(new FileInputStream(bugOut));
        List<ViolationCustomDto> bugs = new ArrayList<>();

        while(true) {
            try {
                bugs.add((ViolationCustomDto) bugInputStream.readObject());
            } catch (EOFException | ClassNotFoundException e) {
                bugInputStream.close();
                break;
            }
        }

        return bugs;
    }
}