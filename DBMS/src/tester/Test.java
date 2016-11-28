package tester;

import java.util.ArrayList;
import java.util.List;

import controller.DBMSController;

public class Test {

    @org.junit.Test
    public void test1() {
        DBMSController db = new DBMSController();
        db.getDatabaseController().createDatabase("Mahmoud");
        List<String> str = new ArrayList<>();
        str.add("col1");
        str.add("Col2");
        str.add("col3");
        List<Class<?>> cls = new ArrayList<>();
        cls.add(str.get(0).getClass());
        cls.add(Integer.class);
        cls.add(Integer.class);
        List<Object> values = new ArrayList<>();
        values.add(new String("Here"));
        values.add(new Integer(5));
        values.add(new Integer(4));
        db.getDatabaseController().useDatabase("Mahmoud");
        db.getDatabaseController().createTable("Moda", str, cls);
        db.getDatabaseController().insertIntoTable("Moda", values);
    }
}
