package controller;

import java.util.List;

public interface DBMS {

    public boolean useDatabase(String databaseName);

    public boolean createDatabase(String databaseName);

    public boolean createTable(String tableName, List<String> colNames, List<Class<?>> types);

    public boolean dropTable(String tableName);

    public boolean dropDatabase(String databaseName);

    public boolean insertIntoTable(String tableName, List<String> colNames, List<Object> values);

    public boolean insertIntoTable(String tableName, List<Object> values);

    public boolean selectFromTable(String tableName, List<String> colNames, String condition);

    public boolean updateTable(String tableName, List<String> colNames, List<Object> values,
            String condition);

    public boolean deleteFromTable(String tableName, String condition);
}
