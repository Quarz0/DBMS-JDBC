package eg.edu.alexu.csd.oop.DBMS.junitTests;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

import eg.edu.alexu.csd.oop.DBMS.model.SQLTypeFactory;
import eg.edu.alexu.csd.oop.DBMS.plugins.jdbc.Driver;

public class JDBCResultSetMetaDataTest {

    @Test
    public void getColumnCounts() throws SQLException {
        Driver driver = new Driver();
        Properties info = new Properties();
        info.setProperty("path", System.getProperty("user.home"));
        String url = "jdbc:xmldb://localhost";
        java.sql.Connection connection = driver.connect(url, info);
        Statement statement = connection.createStatement();
        ResultSet temp;
        statement.execute("create database test");
        statement.execute("use test");
        statement.execute("create table tester (name varchar,id int, salary float)");
        temp = statement.executeQuery("select * from tester");
        assertEquals(temp.getMetaData().getColumnCount(), 3);
        statement.execute("insert into tester values (\"ahmed\",15, 32.5)");
        statement.execute("insert into tester values (\"ahmed\",15, 32.5)");
        statement.execute("alter table tester add lastName varchar");
        temp = statement.executeQuery("select * from tester");
        assertEquals(temp.getMetaData().getColumnCount(), 4);
        statement.execute("alter table tester add age int");
        temp = statement.executeQuery("select * from tester");
        assertEquals(temp.getMetaData().getColumnCount(), 5);
        statement.execute("alter table tester drop column lastName");
        temp = statement.executeQuery("select * from tester");
        assertEquals(temp.getMetaData().getColumnCount(), 4);
        statement.execute("drop database test");
    }

    @Test
    public void getColumnLabel() throws SQLException {
        Driver driver = new Driver();
        Properties info = new Properties();
        info.setProperty("path", System.getProperty("user.home"));
        String url = "jdbc:xmldb://localhost";
        java.sql.Connection connection = driver.connect(url, info);
        Statement statement = connection.createStatement();
        ResultSet temp;
        statement.execute("create database test");
        statement.execute("use test");
        statement.execute("create table tester (name varchar,id int, salary float)");
        temp = statement.executeQuery("select * from tester");
        assertEquals(temp.getMetaData().getColumnLabel(2), "id");
        statement.execute("insert into tester values (\"ahmed\",15, 32.5)");
        statement.execute("insert into tester values (\"ahmed\",15, 32.5)");
        statement.execute("alter table tester add lastName varchar");
        temp = statement.executeQuery("select * from tester");
        assertEquals(temp.getMetaData().getColumnLabel(4), "lastName");
        statement.execute("alter table tester add age int");
        temp = statement.executeQuery("select * from tester");
        assertEquals(temp.getMetaData().getColumnLabel(5), "age");
        statement.execute("alter table tester drop column lastName");
        temp = statement.executeQuery("select * from tester");
        assertEquals(temp.getMetaData().getColumnLabel(4), "age");
        statement.execute("drop database test");
    }

    @Test
    public void getColumnType() throws SQLException {
        Driver driver = new Driver();
        Properties info = new Properties();
        info.setProperty("path", System.getProperty("user.home"));
        String url = "jdbc:xmldb://localhost";
        java.sql.Connection connection = driver.connect(url, info);
        Statement statement = connection.createStatement();
        ResultSet temp;
        statement.execute("create database test");
        statement.execute("use test");
        statement.execute("create table tester (name varchar,id int, salary float)");
        temp = statement.executeQuery("select * from tester");
        assertEquals(temp.getMetaData().getColumnType(2), SQLTypeFactory.getSQLType(Integer.class));
        statement.execute("insert into tester values (\"ahmed\",15, 32.5)");
        statement.execute("insert into tester values (\"ahmed\",15, 32.5)");
        statement.execute("alter table tester add birth date");
        temp = statement.executeQuery("select * from tester");
        assertEquals(temp.getMetaData().getColumnType(4),
                SQLTypeFactory.getSQLType(java.sql.Date.class));
        statement.execute("alter table tester add age int");
        temp = statement.executeQuery("select * from tester");
        assertEquals(temp.getMetaData().getColumnType(1), SQLTypeFactory.getSQLType(String.class));
        statement.execute("drop database test");
    }
}
