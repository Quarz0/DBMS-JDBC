package model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import util.App;

public class TypeFactory {
    public static Class<?> getClass(String dataType) {
        switch (dataType.toLowerCase()) {
        case "int":
        case "integer":
            return Integer.class;
        case "varchar":
        case "string":
            return String.class;
        case "float":
            return Float.class;
        case "boolean":
            return Boolean.class;
        case "date":
            return Date.class;
        case "time":
            return Time.class;
        case "timestamp":
            return Timestamp.class;
        default:
            return null;
        }
    }

    public static Object parseToObject(Class<?> cls, String str) throws ClassCastException {
        if (!App.checkForExistence(str) || !App.checkForExistence(cls))
            throw new ClassCastException("Soon!");
        str = str.trim();
        if (str.equals("null")) {
            return null;
        } else if (Integer.class.equals(cls)) {
            return Integer.valueOf(str);
        } else if (Double.class.equals(cls)) {
            return Double.valueOf(str);
        } else if (Float.class.equals(cls)) {
            return Float.valueOf(str);
        } else if (String.class.equals(cls)) {
            if (str.trim().matches("\".+\"") || str.trim().matches("'.+'"))
                return String.valueOf(str.trim().substring(1, str.trim().length() - 1));
            throw new ClassCastException("I am sorry but strings must be quoted");
        } else if (Boolean.class.equals(cls)) {
            return Boolean.valueOf(str);
        } else if (Character.class.equals(cls)) {
            return Character.valueOf(str.charAt(0));
        } else if (Byte.class.equals(cls)) {
            return Byte.valueOf(str);
        } else if (Date.class.equals(cls)) {
            if (str.trim().matches("\".+\"") || str.trim().matches("'.+'"))
                return Date.valueOf(str.trim().substring(1, str.trim().length() - 1));
            return Date.valueOf(str);
        } else if (Time.class.equals(cls)) {
            return Time.valueOf(str);
        } else if (Timestamp.class.equals(cls)) {
            return Timestamp.valueOf(str);
        } else {
            throw new ClassCastException("Soon!");
        }
    }
}
