package eg.edu.alexu.csd.oop.DBMS.junitTests;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

import eg.edu.alexu.csd.oop.DBMS.model.SQLTypeFactory;
import eg.edu.alexu.csd.oop.DBMS.plugins.jdbc.Driver;

public class TestExtraStatements {

     @Test
     public void testAlter() throws SQLException {
     Driver driver = new Driver();
     Properties info = new Properties();
     info.setProperty("path", System.getProperty("user.home"));
     String url = "jdbc:altdb://localhost";
     java.sql.Connection connection = driver.connect(url, info);
     Statement statement = connection.createStatement();
     ResultSet temp;
     statement.execute("create database test");
     statement.execute("use test");
     statement.execute("create table tester (name varchar,id int, salary float)");
     statement.execute("insert into tester values (\"ahmed\",15, 32.5)");
     statement.execute("insert into tester values (\"ahmed\",15, 32.5)");
     statement.execute("alter table tester add lastName varchar");
     temp = statement.executeQuery("select * from tester");
     temp.next();
     assertEquals(null,temp.getObject(4));
     statement.execute("alter table tester add age int");
     statement.execute("update tester set age = 15");
     temp = statement.executeQuery("select * from tester");
     assertEquals(5,temp.getMetaData().getColumnCount());
     statement.execute("alter table tester modify column salary int");
     temp = statement.executeQuery("select * from tester");
     assertEquals(SQLTypeFactory.getSQLType(Integer.class),temp.getMetaData().getColumnType(3));
     statement.execute("alter table tester drop column name");
     temp = statement.executeQuery("select * from tester");
     assertEquals(4,temp.getMetaData().getColumnCount());
     assertEquals("id",temp.getMetaData().getColumnLabel(1));
     statement.execute("drop database test");
     }

    @Test
    public void testOrderBy() throws SQLException {
        Driver driver = new Driver();
        Properties info = new Properties();
        info.setProperty("path", System.getProperty("user.home"));
        String url = "jdbc:xmldb://localhost";
        java.sql.Connection connection = driver.connect(url, info);
        Statement statement = connection.createStatement();
        statement.execute("create database test");
        statement.execute("use test");
        statement.execute("create table tester (name varchar,id int, salary float)");
        statement.execute("insert into tester values (\"ahmec\", 15, 14.5)");
        statement.execute("insert into tester values (\"ahmed\", 14, 32.5)");
        statement.execute("insert into tester values (\"ahmee\", 12, 10.5)");
        ResultSet temp = statement
                .executeQuery("SELECT name, id, salary from tester order by name ASC, id DESC");
        temp.next();
        assertEquals("ahmec", temp.getString(1));
        assertEquals(15, temp.getInt(2));
        temp.next();
        assertEquals("ahmed", temp.getString(1));
        assertEquals(14, temp.getInt(2));
        temp.next();
        assertEquals("ahmee", temp.getString(1));
        assertEquals(10.5, temp.getFloat(3), 0.001);
        statement.execute("drop database test");
    }

    @Test
    public void testSelectDistint() throws SQLException {
        Driver driver = new Driver();
        Properties info = new Properties();
        info.setProperty("path", System.getProperty("user.home"));
        String url = "jdbc:xmldb://localhost";
        java.sql.Connection connection = driver.connect(url, info);
        Statement statement = connection.createStatement();
        statement.execute("create database test");
        statement.execute("use test");
        statement.execute("create table tester (name varchar,id int, salary float)");
        statement.execute("insert into tester values (\"ahmed\", 15, 14.5)");
        statement.execute("insert into tester values (\"ahmed\", 14, 32.5)");
        statement.execute("insert into tester values (\"ahmed\", 15, 10.5)");
        ResultSet temp = statement
                .executeQuery("SELECT Distinct name, salary from tester");
        temp.next();
        assertEquals(2,temp.getMetaData().getColumnCount());
        assertEquals("ahmed", temp.getString(1));
        assertEquals(14.5, temp.getFloat(2), 0.001);
        temp = statement
                .executeQuery("SELECT Distinct name, id from tester");
        temp.next();
        assertEquals(2,temp.getMetaData().getColumnCount());
        assertEquals(15, temp.getInt(2));
        temp.next();
        assertEquals(14, temp.getInt(2));
        statement.execute("drop database test");
    }

}
