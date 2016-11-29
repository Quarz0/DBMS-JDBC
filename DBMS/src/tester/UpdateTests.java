package tester;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import controller.DBMS;
import controller.DBMSController;
import controller.DatabaseController;

public class UpdateTests {
    private DBMS db;
    private DBMSController dbmsController;

    @Before
    public final void init() {
        db = new DatabaseController(null);
        dbmsController = new DBMSController();
    }
    
    @Test (expected = RuntimeException.class)
    public final void select1() {
        List<String> columns = new ArrayList<>();
        columns.add("Name");
        columns.add("Age");
        columns.add("Married");
        List<Class<?>> types = new ArrayList<>();
        types.add(String.class);
        types.add(Integer.class);
        types.add(Boolean.class);
        db.createTable("Table1", columns, types);
        List<String> values1 = new ArrayList<>();
        values1.add("Rob");
        values1.add("20");
        values1.add("true");
        List<String> values2 = new ArrayList<>();
        values2.add("Edward");
        values2.add("15");
        values2.add("false");
        db.insertIntoTable("Table1", values1);        
        db.insertIntoTable("Table1", values2);
        values2.set(0, "Ed");
        values2.set(1, "1");
        values2.remove(2);
        columns.remove(2);
        db.updateTable("Table1", columns, values2, "married == false");
        String table = "  Table: Table1\n" +
                "+------+-----+---------+\n" +
                "| Name | Age | Married |\n" +
                "+------+-----+---------+\n" +
                "| Rob  | 20  | false   |\n" +
                "| Ed   | 1   | false   |\n" +
                "+------+-----+---------+\n" +
                "  Records: 2\n";
        Assert.assertEquals(table, db.selectFromTable("Table1", columns, null));
        
    }
}
