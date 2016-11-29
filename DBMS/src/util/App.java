package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class App {

    public static final String DEFAULT_DIR_PATH = System.getProperty("user.home")
            + File.separatorChar + "DBMS" + File.separatorChar;
    public static final String PS1 =  "sql>> ";

    public static boolean checkForExistence(Object object) {
        if (object == null)
            return false;

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

    private static boolean isBalancedQuotedString(String s) {
        int cnt = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '"') {
                cnt++;
            }
        }
        return cnt % 2 == 0;
    }

    public static String replace(String s, String _old, String _new) {
        int index = 0;
        int fromIndex = 0;
        while (true) {
            index = s.indexOf(_old, fromIndex);
            fromIndex = index + 1;
            if (index == -1) {
                break;
            }

            String left = s.substring(0, index);
            String right = s.substring(index + _old.length(), s.length());
            boolean notPartOfWord = (index == 0 || !isValidChar(s.charAt(index - 1)))
                    && (index + _old.length() == s.length()
                            || !isValidChar(s.charAt(index + _old.length())));

            if (notPartOfWord && isBalancedQuotedString(left) && isBalancedQuotedString(right)) {
                s = left + _new + right;
            }
        }
        return s;
    }

    private static boolean isValidChar(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_' || (c >= '0' && c <= 9);
    }

    private App() {
    }
}
