package eg.edu.alexu.csd.oop.DBMS.junitTests;

public class JDBCStatementTest {

    // @Test
    // public void testBatch() throws SQLException {
    // Driver driver = new Driver();
    // Properties info = new Properties();
    // info.setProperty("path", System.getProperty("user.home"));
    // String url = "jdbc:xmldb://localhost";
    // java.sql.Connection connection = driver.connect(url, info);
    // Statement statement = connection.createStatement();
    // statement.addBatch("create database test");
    // statement.addBatch("create database test");
    // statement.addBatch("use test");
    // statement.addBatch("use tester");
    // statement.addBatch("create table tester (name varchar,id int)");
    // statement.addBatch("insert into tester values (\"ahmed\",15)");
    // statement.addBatch("insert into tester (\"ahmed\",15)");
    // statement.addBatch("select * from tester");
    // statement.addBatch("drop database test");
    // int[] arr = statement.executeBatch();
    // int[] testArr = { 0, java.sql.Statement.EXECUTE_FAILED, 0,
    // java.sql.Statement.EXECUTE_FAILED, 0, 1, java.sql.Statement.EXECUTE_FAILED,
    // java.sql.Statement.SUCCESS_NO_INFO, 0 };
    // assertEquals(arr,testArr);
    // statement.close();
    // connection.close();
    // }

    // @Test
    // public void testExecute() throws SQLException {
    // Driver driver = new Driver();
    // Properties info = new Properties();
    // info.setProperty("path", System.getProperty("user.home"));
    // String url = "jdbc:xmldb://localhost";
    // java.sql.Connection connection = driver.connect(url, info);
    // Statement statement = connection.createStatement();
    // assertFalse(statement.execute("create database test"));
    // assertFalse(statement.execute("use test"));
    // assertFalse(statement.execute("create table tester (name varchar,id int)"));
    // assertFalse(statement.execute("insert into tester values (\"ahmed\",15)"));
    // assertTrue(statement.execute("select * from tester"));
    // assertFalse(statement.execute("drop database test"));
    // connection.close();
    // }
    //
    // @Test
    // public void testExecuteQuery() throws SQLException {
    // Driver driver = new Driver();
    // Properties info = new Properties();
    // info.setProperty("path", System.getProperty("user.home"));
    // String url = "jdbc:xmldb://localhost";
    // java.sql.Connection connection = driver.connect(url, info);
    // Statement statement = connection.createStatement();
    // statement.execute("create database test");
    // statement.execute("use test");
    // statement.execute("create table tester (name varchar,id int, salary float)");
    // statement.execute("insert into tester values (\"ahmed\",15, 32.5)");
    // ResultSet temp = statement.executeQuery("select * from tester");
    // assertNotEquals(null,temp);
    // assertEquals(temp,statement.getResultSet());
    // statement.execute("drop database test");
    // }

    // @Test
    // public void testExecuteUpdate() throws SQLException {
    // Driver driver = new Driver();
    // Properties info = new Properties();
    // info.setProperty("path", System.getProperty("user.home"));
    // String url = "jdbc:xmldb://localhost";
    // java.sql.Connection connection = driver.connect(url, info);
    // Statement statement = connection.createStatement();
    // assertEquals(statement.executeUpdate("create database test"),0);
    // assertEquals(statement.getUpdateCount(),0);
    // assertEquals(statement.executeUpdate("use test"),0);
    // assertEquals(statement.getUpdateCount(),0);
    // assertEquals(statement.executeUpdate("create table tester (name varchar,id int, salary
    // float)"),0);
    // assertEquals(statement.getUpdateCount(),0);
    // assertEquals(statement.executeUpdate("insert into tester values (\"ahmed\",15, 32.5)"),1);
    // assertEquals(statement.getUpdateCount(),1);
    // assertEquals(statement.executeUpdate("insert into tester values (\"ahmed\",15, 32.5)"),1);
    // assertEquals(statement.getUpdateCount(),1);
    // assertEquals(statement.executeUpdate("alter table tester add lastName varchar"),0);
    // assertEquals(statement.getUpdateCount(),0);
    // assertEquals(statement.executeUpdate("alter table tester add age int"),0);
    // assertEquals(statement.getUpdateCount(),0);
    // assertEquals(statement.executeUpdate("alter table tester drop column lastName"),0);
    // assertEquals(statement.getUpdateCount(),0);
    // assertEquals(statement.executeUpdate("update tester set age = 20"),2);
    // assertEquals(statement.getUpdateCount(),2);
    // assertEquals(statement.executeUpdate("insert into tester values (\"AhmedTany\", 20, 35.99,
    // 12)"),1);
    // assertEquals(statement.getUpdateCount(),1);
    // assertEquals(statement.executeUpdate("drop database test"),0);
    // assertEquals(statement.getUpdateCount(),0);
    // }

}
