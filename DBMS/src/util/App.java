package util;

import java.util.ArrayList;
import java.util.List;

public final class App {

    public static final String DEFAULT_DIR_NAME = System.getProperty("user.name") + "_DBMS/";
    public static final String PS1 = System.getProperty("user.home") + "/" + DEFAULT_DIR_NAME
            + ">> ";

    public static boolean checkForExistence(Object object) {
        if (object instanceof String)
            return !(object.equals(null) || ((String) object).isEmpty());
        return !object.equals(null);
    }

    public static Object[] subArray(Object[] objects, int startIndex, int endIndex) {
        List<Object> result = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++)
            result.add(objects[i]);
        return result.toArray();
    }

    private App() {
    }
}
