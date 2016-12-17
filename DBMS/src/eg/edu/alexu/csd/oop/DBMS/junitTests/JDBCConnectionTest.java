package eg.edu.alexu.csd.oop.DBMS.junitTests;

import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

import eg.edu.alexu.csd.oop.DBMS.plugins.jdbc.Driver;

public class JDBCConnectionTest {

    @Test(expected = SQLException.class)
    public void testUsingStatemenThroughClosedConnection() throws SQLException {
        Driver driver = new Driver();
        java.sql.Connection connection = driver.connect("jdbc:altdb://localhost", new Properties());
        java.sql.Statement statement = connection.createStatement();
        statement.executeQuery("create Database school");
        statement.executeQuery("use school");
        statement.executeQuery("create table students(id int, Name varchar, Birthday Date))");
        statement.executeQuery("insert into students values(1,\"Bill\", 2008-07-27)");
        connection.close();
        statement.executeQuery("select * from students");
    }
}
