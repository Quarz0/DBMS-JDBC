package test;

import java.util.ArrayList;
import java.util.List;

import controller.DBMSController;

public class Test {

    @org.junit.Test
    public void test1() {
        DBMSController db = new DBMSController();
        // db.getDatabaseController().createDatabase("Mahmoud");
        List<String> str = new ArrayList<>();
        str.add("col1");
        str.add("col2");
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
        // db.getDatabaseController().createTable("Modaa", str, cls);
        List<String> sellectedCols = new ArrayList<>();
        sellectedCols.add("col4");
        sellectedCols.add("col2");
        sellectedCols.add("col1");
        sellectedCols.add("col3");
        List<Object> sellectedValues = new ArrayList<>();
        sellectedValues.add("lol");
        sellectedValues.add(4);
        sellectedValues.add("lol2");
        sellectedValues.add(6);
        // values.add(new String("Here"));
        // db.getDatabaseController().insertIntoTable("Modaa", sellectedCols, sellectedValues);
        List<String> temp = new ArrayList<>();
        temp.add("col1");
        temp.add("col3");
        temp.add("coL4");
        // db.getDatabaseController().createTable("Moda", str, cls);
        // db.getDatabaseController().insertIntoTable("Moda", values);
        // db.getDatabaseController().insertIntoTable("Moda", values);

        List<String> lst = new ArrayList<>();
        lst.add("col1");
        lst.add("col2");
        List<Object> obj = new ArrayList<>();
        obj.add("heree");
        obj.add(100);
        db.getDatabaseController().deleteFromTable("Modaa", "col1 == \"here\" || col2 == \"null\"");
        // boolean t = db.getDatabaseController().selectFromTable("Modaa", temp,
        // "1==1");
        // SelectionTable tempp = db.getDatabaseController().getHelper().getSelectedTable();
        // for (int i = 0; i < tempp.getHeader().size(); i++) {
        // for (int j = 0; j < tempp.getRecordList().get(i).getValues().size(); j++) {
        // System.out.print(tempp.getRecordList().get(i).getValues().get(j) + " ");
        // }
        // System.out.println();
        // }
    }
}
