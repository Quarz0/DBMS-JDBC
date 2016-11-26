package model;

import java.sql.Time;
import java.sql.Date;

public class ClassFactroy {

    public Class<?> getClass(String dataType) {
        switch (dataType.toLowerCase()) {
        case "int":
        case "integer":
            return Integer.class;
        case "varchar":
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
