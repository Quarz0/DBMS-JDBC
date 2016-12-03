package controller;

import java.util.List;

public interface DBMS {

    public boolean createDatabase(String databaseName);

    public boolean createTable(String tableName, List<String> colNames, List<Class<?>> types);

    public boolean deleteFromTable(String tableName, String condition);

    public boolean dropDatabase(String databaseName);

    public boolean dropTable(String tableName);

    public boolean insertIntoTable(String tableName, List<String> values);

    public boolean insertIntoTable(String tableName, List<String> colNames, List<String> values);

    public String selectFromTable(String tableName, List<String> colNames, String condition);

    public boolean updateTable(String tableName, List<String> colNames, List<String> values,
            String condition);

    public boolean useDatabase(String databaseName);
}
