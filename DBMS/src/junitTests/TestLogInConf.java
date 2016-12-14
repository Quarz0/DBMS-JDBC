package junitTests;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import plugins.jdbc.Connection;

public class TestLogInConf {

    @Test
    public void test() {
        Method method = null;
        try {
            method = Connection.class.getMethod("canLogIn");
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        Boolean bol = false;
        try {
            method.invoke(bol, "Ahmed", "Mahmoud");
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        assertTrue(bol);
    }
}
