package org.github.babkiniaa.scas.utils.analysis;

import com.puppycrawl.tools.checkstyle.*;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.reporting.RuleViolation;

import org.github.babkiniaa.scas.reporters.MyList;
import org.owasp.dependencycheck.Engine;
import org.owasp.dependencycheck.agent.DependencyCheckScanAgent;
import org.owasp.dependencycheck.dependency.Dependency;
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

public class StaticAnalysis {

  public static void startPmd(String path) throws Exception {
//    ProcessBuilder processBuilder = new ProcessBuilder();
//    String dirReport = "-DdistPMD=" + nameFile;
//    processBuilder.command(
//            System.getenv("M2_HOME") + "\\bin\\mvn.cmd",
//            dirReport,
//            "pmd:pmd");
//
//    try {
//      Process process = processBuilder.start();
//      BufferedReader reader =
//              new BufferedReader(new InputStreamReader(process.getInputStream()));
//      String line;
//      while ((line = reader.readLine()) != null) {
//      }
//      int exitCode = process.waitFor();
//      process.destroy();
//    } catch (InterruptedException | IOException e) {
//      e.printStackTrace();
//      throw e;
//    }

     PMDConfiguration config = new PMDConfiguration();

     config.setDefaultLanguageVersion(LanguageRegistry.PMD.getLanguageVersionById("java", null));
     config.addRuleSet("rulesets/java/quickstart.xml");

     try (PmdAnalysis pmd = PmdAnalysis.create(config)) {

       pmd.files().addDirectory(Path.of(path), true);
       List<RuleViolation> ruleViolationList = pmd.performAnalysisAndCollectReport().getViolations();

     }

  }

  public static void startOWASP(String scanDir) throws IOException, InterruptedException {
//    ProcessBuilder processBuilder = new ProcessBuilder();
//    processBuilder.directory(new File(scanDir));
//    processBuilder.command(
//            System.getenv("M2_HOME") + "\\bin\\mvn.cmd",
//            "org.owasp:dependency-check-maven:check"
//    );
//
//    try {
//      Process process = processBuilder.start();
//      BufferedReader reader =
//              new BufferedReader(new InputStreamReader(process.getInputStream()));
//      String line;
//      while ((line = reader.readLine()) != null) {
//      }
//      int exitCode = process.waitFor();
//      process.destroy();
//    } catch (InterruptedException | IOException e) {
//      e.printStackTrace();
//      throw e;
//    }

    Engine engine = new Engine(new Settings());
    List<Dependency> list2 = engine.scan(scanDir);
    DependencyCheckScanAgent scan = new DependencyCheckScanAgent();
    scan.setDataDirectory("C:\\dependency-check\\data");
    scan.setDependencies(list2);
//    scan.setReportFormat(ReportGenerator.Format.HTML);
//    scan.setReportOutputDirectory(System.getProperty("user.home"));
    scan.execute();
//        engine.analyzeDependencies();
    Dependency[] dependencies = (Dependency[]) Arrays.stream(engine.getDependencies()).filter(x -> x.getVulnerabilitiesCount() > 0).toArray();

  }

  public static void startCheckStyle(String path) throws Exception {
//    ProcessBuilder processBuilder = new ProcessBuilder();
//    String dirReport = "-DdistCheckerStyle=" + nameFile;
//    processBuilder.command(
//            System.getenv("M2_HOME") + "\\bin\\mvn.cmd",
//            dirReport,
//            "checkstyle:checkstyle"
//    );
//
//    try {
//      Process process = processBuilder.start();
//      BufferedReader reader =
//              new BufferedReader(new InputStreamReader(process.getInputStream()));
//      String line;
//      while ((line = reader.readLine()) != null) {
//      }
//      int exitCode = process.waitFor();
//      process.destroy();
//    } catch (InterruptedException | IOException e) {
//      e.printStackTrace();
//      throw e;
//    }

    String[] arg = new String[2];
    arg[0] = "C:\\test\\spotTusk\\src\\main\\java";
    arg[1] = "-c=checkstyle.xml";
    Main.main(arg);
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

  }
}