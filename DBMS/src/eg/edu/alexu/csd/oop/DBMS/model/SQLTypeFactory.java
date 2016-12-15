package eg.edu.alexu.csd.oop.DBMS.model;

import java.sql.Types;

public class SQLTypeFactory {
    public static int getSQLType(Class<?> cls) throws ClassCastException {
        if (Integer.class.equals(cls)) {
            return Types.INTEGER;
        } else if (Double.class.equals(cls)) {
            return Types.DOUBLE;
        } else if (Float.class.equals(cls)) {
            return Types.FLOAT;
        } else if (String.class.equals(cls)) {
            return Types.VARCHAR;
        } else if (Boolean.class.equals(cls)) {
            return Types.BOOLEAN;
        } else if (Character.class.equals(cls)) {
            return Types.CHAR;
        } else if (java.sql.Date.class.equals(cls)) {
            return Types.DATE;
        } else if (java.sql.Time.class.equals(cls)) {
            return Types.TIME;
        } else {
            return Types.OTHER;
        }
    }
}
