package tester;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import controller.DBMS;
import controller.DatabaseController;

public class DeleteTests {
    private DBMS db;

    @Before
    public final void init() {
        db = new DatabaseController(null);
    }

    @Test(expected = RuntimeException.class)
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
        List<Object> values1 = new ArrayList<>();
        values1.add("Rob");
        values1.add(20);
        values1.add(false);
        List<Object> values2 = new ArrayList<>();
        values2.add("Edward");
        values2.add(15);
        values2.add(false);
        // db.insertIntoTable("Table1", values1);
        // db.insertIntoTable("Table1", values2);
        db.deleteFromTable("Table1", "age <= 15");
        columns.remove(2);
        String table = "  Table: Table1\n" + "+------+-----+\n" + "| Name | Age |\n"
                + "+------+-----+\n" + "| Rob  | 20  |\n" + "+------+-----+\n" + "  Records: 1\n\n";
        Assert.assertEquals(table, db.selectFromTable("Table1", columns, "age != 20"));
        columns.remove(1);
        // db.insertIntoTable("Table1", values2);
        db.deleteFromTable("Table1", null);
        table = "  Table: Table1\n" + "+------+\n" + "| Name |\n" + "+------+\n"
                + "  Records: 0\n\n";
        Assert.assertEquals(table, db.selectFromTable("Table1", columns, null));
    }
}
