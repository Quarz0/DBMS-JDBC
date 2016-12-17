package eg.edu.alexu.csd.oop.DBMS.junitTests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

import eg.edu.alexu.csd.oop.DBMS.plugins.jdbc.Driver;

public class JDBCStatementTest {

//    @Test
//    public void testBatch() throws SQLException {
//        Driver driver = new Driver();
//        Properties info = new Properties();
//        info.setProperty("path", System.getProperty("user.home"));
//        String url = "jdbc:xmldb://localhost";
//        java.sql.Connection connection = driver.connect(url, info);
//        Statement statement = connection.createStatement();
//        statement.addBatch("create database test");
//        statement.addBatch("create database test");
//        statement.addBatch("use test");
//        statement.addBatch("use tester");
//        statement.addBatch("create table tester (name varchar,id int)");
//        statement.addBatch("insert into tester values (\"ahmed\",15)");
//        statement.addBatch("insert into tester (\"ahmed\",15)");
//        statement.addBatch("select * from tester");
//        statement.addBatch("drop database test");
//        int[] arr = statement.executeBatch();
//        int[] testArr = { 0, java.sql.Statement.EXECUTE_FAILED, 0,
//                java.sql.Statement.EXECUTE_FAILED, 0, 1, java.sql.Statement.EXECUTE_FAILED,
//                java.sql.Statement.SUCCESS_NO_INFO, 0 };
//        assertEquals(arr,testArr);
//        statement.close();
//        connection.close();
//    }

    @Test
    public void testExecute() throws SQLException {
        Driver driver = new Driver();
        Properties info = new Properties();
        info.setProperty("path", System.getProperty("user.home"));
        String url = "jdbc:xmldb://localhost";
        java.sql.Connection connection = driver.connect(url, info);
        Statement statement = connection.createStatement();
        assertFalse(statement.execute("create database test"));
        assertFalse(statement.execute("use test"));
        assertFalse(statement.execute("create table tester (name varchar,id int)"));
        assertFalse(statement.execute("insert into tester values (\"ahmed\",15)"));
        assertTrue(statement.execute("select * from tester"));
        assertFalse(statement.execute("drop database test"));
        connection.close();
    }
    
    @Test
    public void tesõtõExecuteQuery() throws SQLException {
        Driver driver = new Driver();
        Properties info = new Properties();
        info.setProperty("path", System.getProperty("user.home"));
        String url = "jdbc:xmldb://localhost";
        java.sql.Connection connection = driver.connect(url, info);
        Statement statement = connection.createStatement();
        statement.execute("create database test");
        statement.execute("use test");
        statement.execute("create table tester (name varchar,id int, salary float)");
        statement.execute("insert into tester values (\"ahmed\",15, 32.5)");
        ResultSet temp = statement.executeQuery("select * from tester");
        assertNotEquals(null,temp);
        assertEquals(temp,statement.getResultSet());
        statement.execute("drop database test");
    }
    
    
    @Test
    public void tesõtõExecuteUpdate() throws SQLException {
        Driver driver = new Driver();
        Properties info = new Properties();
        info.setProperty("path", System.getProperty("user.home"));
        String url = "jdbc:xmldb://localhost";
        java.sql.Connection connection = driver.connect(url, info);
        Statement statement = connection.createStatement();
        
        statement.executeUpdate("create database test");
        statement.executeUpdate("use test");
        statement.executeUpdate("create table tester (name varchar,id int, salary float)");
        statement.executeUpdate("insert into tester values (\"ahmed\",15, 32.5)");
        statement.executeUpdate("drop database test");
        statement.executeUpdate("alter table tester add lastName varchar");
        statement.executeUpdate("alter table tester add age int");
        statement.executeUpdate("alter table tester drop column lastName");
        statement.executeUpdate("update tester set age = 20");
        statement.executeUpdate("insert into tester values (\"AhmedTany\", 20, 35.99, 12)");
        statement.executeUpdate("drop database test");
    }
}
