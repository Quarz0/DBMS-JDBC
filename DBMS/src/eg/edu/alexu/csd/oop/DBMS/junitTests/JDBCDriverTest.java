package eg.edu.alexu.csd.oop.DBMS.junitTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

import eg.edu.alexu.csd.oop.DBMS.plugins.jdbc.Driver;

public class JDBCDriverTest {

    @Test
    public void testAcceptsURL() throws SQLException {
        Driver testDriver = new Driver();
        try {
            assertTrue(testDriver.acceptsURL("jdbc:xmldb://localhost"));
            assertTrue(testDriver.acceptsURL("jdbc:altdb://localhost"));
            assertFalse(testDriver.acceptsURL("jdbcxmldb://localhost"));
            assertTrue(testDriver.acceptsURL("jdbc:jsondb://localhost"));
            assertTrue(testDriver.acceptsURL("jdbc:protodb://localhost"));
            assertFalse(testDriver.acceptsURL("jdbc:pbdb://localhost"));
            assertFalse(testDriver.acceptsURL("jdbc:db://localhost"));
            assertFalse(testDriver.acceptsURL("jdbc:xmlldb://localhost"));
            assertFalse(testDriver.acceptsURL(null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testConnect() {
        Properties info = new Properties();
        info.setProperty("username", "ahmed");
        info.setProperty("password", "pass");
    }

}
