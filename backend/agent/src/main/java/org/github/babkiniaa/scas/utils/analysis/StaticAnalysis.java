package org.github.babkiniaa.scas.utils.analysis;

import com.puppycrawl.tools.checkstyle.*;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Violation;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.document.FileLocation;
import net.sourceforge.pmd.lang.rule.Rule;
import net.sourceforge.pmd.reporting.RuleViolation;

import org.github.babkiniaa.scas.reporters.MyList;
import org.owasp.dependencycheck.Engine;
import org.owasp.dependencycheck.agent.DependencyCheckScanAgent;
import org.owasp.dependencycheck.dependency.Dependency;
import org.owasp.dependencycheck.exception.ExceptionCollection;
import org.owasp.dependencycheck.reporting.ReportGenerator;
import org.owasp.dependencycheck.utils.Settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StaticAnalysis {

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

    public static List<Violation> startCheckStyle(String path) throws Exception {
        Checker checker = new Checker();
        Configuration config2 = ConfigurationLoader.loadConfiguration("checkstyle.xml",
                new PropertiesExpander(System.getProperties()), ConfigurationLoader.IgnoredModulesOptions.OMIT,
                new ThreadModeSettings(1, 1));
        ClassLoader moduleClassLoader = Checker.class.getClassLoader();
        ModuleFactory factory = new PackageObjectFactory(Checker.class.getPackage().getName(), moduleClassLoader);
//        RootModule factory.createModule(config.getName()); (new DefaultLogger(System.out, AbstractAutomaticBean.OutputStreamOptions.CLOSE))
        checker.setModuleFactory(factory);
        checker.configure(config2);
        checker.addListener(new MyList());
        List<File> list = new ArrayList<File>();
        list.add(new File(path));
        int process = checker.process(list);
        return null;
    }
}