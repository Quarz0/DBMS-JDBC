package util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public final class BooleanEvaluator {
    
    private static ScriptEngineManager mgr;
    private static ScriptEngine engine;
    
    private BooleanEvaluator() {};
    
    private static void initialize() {
        if (mgr != null) return;
        mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");
    }
    
    public static boolean evaluate(String exp) throws ScriptException {
        initialize();
        exp = exp.toLowerCase();
        exp = App.replace(exp, "and", " && ");
        exp = App.replace(exp, "or", " || ");
        exp = App.replace(exp, "not", " ! ");

        return (Boolean) engine.eval(exp);
    }
        
}
