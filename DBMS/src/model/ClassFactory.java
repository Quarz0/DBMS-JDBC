package model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class ClassFactory {

    public static Class<?> getClass(String dataType) {
        switch (dataType.toLowerCase()) {
        case "int":
        case "integer":
            return Integer.class;
        case "varchar":
        case "string":
            return String.class;
        case "float":
            return Double.class;
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
}