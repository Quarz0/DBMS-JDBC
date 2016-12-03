package util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public final class BooleanEvaluator {

    private static ScriptEngine engine;
    private static ScriptEngineManager mgr;

    public static boolean evaluate(String exp) throws ScriptException {
        BooleanEvaluator.initialize();
        return (Boolean) engine.eval(exp);
    };

    private static void initialize() {
        if (mgr != null)
            return;
        mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");
    }

    private BooleanEvaluator() {
    }

}
