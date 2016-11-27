package tester;

import java.util.ArrayList;
import java.util.List;

import controller.DBMSController;

public class Test {

    @org.junit.Test
    public void test() {
        DBMSController db = new DBMSController();
        db.getDatabaseController().createDatabase("Mahmoud");
        List<String> str = new ArrayList<>();
        str.add("col1");
        str.add("Col2");
        List<Class<?>> cls = new ArrayList<>();
        cls.add(str.get(0).getClass());
        cls.add(Integer.class);
        db.getDatabaseController().useDatabase("Mahmoud");
        db.getDatabaseController().createTable("7oda", str, cls);
    }

}
