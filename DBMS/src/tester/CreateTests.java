package tester;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import controller.DBMS;
import controller.DatabaseController;

public class CreateTests {

    private DBMS db;

    @Before
    public final void init() {
        db = new DatabaseController(null);
    }

    /**
     * Exception.
     */
    @Test(expected = RuntimeException.class)
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
    }

    @Test(expected = RuntimeException.class)
    public final void createTableWithoutUsingDatabase() {
        db.createDatabase("Database1");
        List<String> columns = new ArrayList<>();
        columns.add("Name");
        List<Class<?>> types = new ArrayList<>();
        types.add(String.class);
        db.createTable("Table1", columns, types);
    }

    @Test(expected = RuntimeException.class)
    public final void usingNonExistingDatabase() {
        db.createDatabase("Database1");
        db.useDatabase("Database2");
    }

    @Test
    public final void creatingAndUsingDatabase() {
        db.createDatabase("Database1");
        Assert.assertEquals(true, db.useDatabase("Database1"));
    }

    @Test
    public final void creatingDatabaseAndTable() {
        Assert.assertEquals(true, db.createDatabase("Database1"));
        List<String> columns = new ArrayList<>();
        columns.add("Name");
        List<Class<?>> types = new ArrayList<>();
        types.add(String.class);
        Assert.assertEquals(true, db.useDatabase("Database1"));
        Assert.assertEquals(true, db.createTable("Table1", columns, types));
    }

}