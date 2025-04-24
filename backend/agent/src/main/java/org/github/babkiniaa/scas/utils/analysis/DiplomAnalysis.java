package org.github.babkiniaa.scas.utils.analysis;

import org.jara.core.Attentions;
import org.jara.engine.Engine;
import org.jara.mode.*;

import java.util.List;

public class DiplomAnalysis {

    public static List<Attentions> startCopy(String path, Mode mode) {
        List<Attentions> attentions;
        Settings config = new Settings();
        config.setInputDir(path);
        if (mode != null) {
            config.setMode(mode);
        }
        Engine engine = new Engine(config);

        engine.scan();
        attentions = engine.getAttentions();

        return attentions;
    }


}
