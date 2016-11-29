package model;

public class ObjectFactory {
    public Object parseToObject(Class<?> cls, String str) {
        if (Integer.class == cls) {
            return Integer.valueOf(str);
        } else if (Double.class == cls) {
            return Double.valueOf(str);
        } else if (Float.class == cls) {
            return Float.valueOf(str);
        } else if (String.class == cls) {
            return String.valueOf(str);
        } else if (Boolean.class == cls) {
            return Boolean.valueOf(str);
        } else if (Character.class == cls) {
            return Character.valueOf(str.charAt(0));
        } else if (Byte.class == cls) {
            return Byte.valueOf(str);
        } else {
            return str;
        }
    }
}
