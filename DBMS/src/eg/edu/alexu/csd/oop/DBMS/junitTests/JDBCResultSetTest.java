package eg.edu.alexu.csd.oop.DBMS.junitTests;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

import eg.edu.alexu.csd.oop.DBMS.plugins.jdbc.Driver;

public class JDBCResultSetTest {

//    @Test
//    public void testCursorMovements() throws SQLException {
//        Driver driver = new Driver();
//        Properties info = new Properties();
//        info.setProperty("path", System.getProperty("user.home"));
//        String url = "jdbc:xmldb://localhost";
//        java.sql.Connection connection = driver.connect(url, info);
//        Statement statement = connection.createStatement();
//        statement.execute("create database test");
//        statement.execute("use test");
//        statement.execute("create table tester (name varchar,id int, salary float)");
//        statement.execute("insert into tester values (\"ahmed\",15, 32.5)");
//        statement.execute("insert into tester (id, salary) values (15, 12.9)");
//        ResultSet temp = statement.executeQuery("select * from tester");
//        statement.execute("drop database test");
//        assertEquals(true,temp.isBeforeFirst());    //header
//        assertEquals(true,temp.next());             //1
//        assertEquals(true,temp.next());             //2
//        assertEquals(true,temp.isLast());           
//        assertEquals(true,temp.next());             //tail(after last)
//        assertEquals(true,temp.isAfterLast());      
//        assertEquals(false,temp.next());            //failed
//        assertEquals(true,temp.previous());         //2
//        assertEquals(true,temp.first());            //0
//        assertEquals(true,temp.isFirst());
//        assertEquals(true,temp.last());             //2
//        assertEquals(true,temp.isLast());           
//    }
    
    @Test
    public void testGetters() throws SQLException {
        Driver driver = new Driver();
        Properties info = new Properties();
        info.setProperty("path", System.getProperty("user.home"));
        String url = "jdbc:xmldb://localhost";
        java.sql.Connection connection = driver.connect(url, info);
        Statement statement = connection.createStatement();
        statement.execute("create database test");
        statement.execute("use test");
        statement.execute("create table tester (name varchar, id int, salary float, birth Date)");
        statement.execute("insert into tester values (\"ahmed\",15, 32.5, 1996-5-16)");
        statement.execute("insert into tester (id, salary, birth) values (15, 12.9, 1995-12-24)");
        ResultSet temp = statement.executeQuery("select * from tester");
        temp.next();    //1
        assertEquals("ahmed",temp.getString(1));
        assertEquals("ahmed",temp.getString("name"));
        assertEquals(15,temp.getInt(2));
        assertEquals(15,temp.getInt("id"));
        assertEquals(32.5,temp.getFloat(3),0.001);
        assertEquals(32.5,temp.getFloat("salary"),0.001);
        assertEquals(1996-5-16,temp.getDate(4));
        assertEquals(1996-5-16,temp.getDate("birth"));
        temp.next();
        assertEquals(0,temp.getString(1));  //null
        statement.execute("drop database test");
        
    }
    
    

}
