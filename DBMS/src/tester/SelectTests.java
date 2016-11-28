package tester;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controller.DBMS;
import controller.DatabaseController;

public class SelectTests {
    private DBMS db;

    @Before
    public final void init() {
        db = new DatabaseController(null);
    }
    
    @Test (expected = RuntimeException.class)
    public final void createTableWithoutDatabase() {
        List<String> columns = new ArrayList<>();
        columns.add("Name");
        columns.add("Age");
        columns.add("Married");
        List<Class<?>> types = new ArrayList<>();
        types.add(String.class);
        types.add(Integer.class);
        types.add(Boolean.class);
        db.createTable("Table1", columns, types);
        List<Object> values = new ArrayList<>();
        values.add("Rob");
        values.add(20);
        values.add(false);
        db.insertIntoTable("Table1", values);
    }
}
