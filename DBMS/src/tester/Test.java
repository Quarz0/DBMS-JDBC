package tester;

import java.util.ArrayList;
import java.util.List;

import controller.DBMSController;
import model.SelectionTable;

public class Test {

    @org.junit.Test
    public void test1() {
        DBMSController db = new DBMSController();
        // db.getDatabaseController().createDatabase("Mahmoud");
        List<String> str = new ArrayList<>();
        str.add("col1");
        str.add("Col2");
        str.add("col3");
        str.add("col4");
        List<Class<?>> cls = new ArrayList<>();
        cls.add(String.class);
        cls.add(Integer.class);
        cls.add(Integer.class);
        cls.add(String.class);
        List<Object> values = new ArrayList<>();
        values.add(new String("Here"));
        values.add(new Integer(5));
        values.add(new Integer(4));
        values.add(new String("There"));
        db.getDatabaseController().useDatabase("Mahmoud");
        // db.getDatabaseController().createTable("Mod2a", str, cls);
        List<String> sellectedCols = new ArrayList<>();
        sellectedCols.add("col3");
        sellectedCols.add("Col2");
        List<Object> sellectedValues = new ArrayList<>();
        sellectedValues.add(4);
        sellectedValues.add("lol");
        // values.add(new String("Here"));
//        db.getDatabaseController().insertIntoTable("Mod2a", sellectedCols, sellectedValues);
        List<String> temp = new ArrayList<>();
        temp.add("col1");
        temp.add("col3");
        // db.getDatabaseController().createTable("Moda", str, cls);
        // db.getDatabaseController().insertIntoTable("Moda", values);
        // db.getDatabaseController().insertIntoTable("Moda", values);
        boolean t = db.getDatabaseController().selectFromTable("Moda", temp, "1 == 1");
    }
}
