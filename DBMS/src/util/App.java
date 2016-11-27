package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class App {

    public static final String DEFAULT_DIR_PATH = System.getProperty("user.home") + File.separatorChar
            + "DBMS" + File.separatorChar;
    public static final String PS1 = System.getProperty("user.home") + "/" + DEFAULT_DIR_PATH
            + ">> ";

    public static boolean checkForExistence(Object object) {
        if (object instanceof String)
            return !(object.equals(null) || ((String) object).isEmpty());
        return !object.equals(null);
    }

    public static Object[] swapArrayOfTwo(Object[] objects) {
        List<Object> result = new ArrayList<>();
        result.add(objects[1]);
        result.add(objects[0]);
        return result.toArray();
    }

    public static boolean isColumnIdentifier(String s) {
        return s.matches(Regex.LEGAL_IDENTIFIER);
    }

    private App() {
    }
}
