package util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class App {

    public static final String DEFAULT_DIR_PATH = System.getProperty("user.home")
            + File.separatorChar + "DBMS" + File.separatorChar;
    public static final String PS1 = "sql>> ";
    private static final Map<Character, String> specialCharacters;
    static {
        Map<Character, String> temp = new HashMap<>();
        temp.put('(', "\\\\(");
        temp.put(')', "\\\\)");
        specialCharacters = Collections.unmodifiableMap(temp);
    };

    public static boolean checkForExistence(Object object) {
        if (object == null)
            return false;

        if (object instanceof String)
            return !(((String) object).isEmpty());
        return true;
    }

    public static boolean isLegalIdentifier(String s) {
        return s.matches(Regex.LEGAL_IDENTIFIER);
    }

    public static String replace(String s, String _old, String _new) {
        if (!App.checkForExistence(s) || !App.checkForExistence(_old)
                || !App.checkForExistence(_new))
            return null;
        for (Iterator<Character> iterator = specialCharacters.keySet().iterator(); iterator
                .hasNext();) {
            Character entry = iterator.next();
            String value = specialCharacters.get(entry);
            _old = _old.replaceAll("\\" + entry.toString(), value);
        }
        return s.replaceAll(_old + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", _new);
    }

    public static Object[] swapArrayOfTwo(Object[] objects) {
        List<Object> result = new ArrayList<>();
        result.add(objects[1]);
        result.add(objects[0]);
        return result.toArray();
    }

    public static boolean equalStrings(String str1, String str2) {
        return str1.toLowerCase().equals(str2.toLowerCase());
    }

    private App() {
    }
}
