package model;

import java.sql.Date;
import java.sql.Time;

public class ClassFactory {

    public Class<?> getClass(String dataType) {
        switch (dataType.toLowerCase()) {
        case "int":
        case "integer":
            return Integer.class;
        case "varchar":
            return String.class;
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
        default:
            return null;
        }
    }
}