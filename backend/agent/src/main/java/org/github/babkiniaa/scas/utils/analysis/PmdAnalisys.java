package org.github.babkiniaa.scas.utils.analysis;

import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.reporting.RuleViolation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class PmdAnalisys {
    public static List<RuleViolation> createPmd(String path){
        PMDConfiguration config = new PMDConfiguration();

        config.setDefaultLanguageVersion(LanguageRegistry.PMD.getLanguageVersionById("java", null));
        config.addRuleSet("rulesets/java/quickstart.xml");

        try (PmdAnalysis pmd = PmdAnalysis.create(config)) {

            try {
                pmd.files().addDirectory(Path.of(path), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return pmd.performAnalysisAndCollectReport().getViolations();

        }
    }
}
