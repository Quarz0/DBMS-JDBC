package controller;

import java.util.List;

public interface DBMS {

    public void createDatabase(String databaseName) throws RuntimeException;

    public void createTable(String tableName, List<String> colNames, List<Class<?>> types)
            throws RuntimeException;

    public void deleteFromTable(String tableName) throws RuntimeException;

    public void dropDatabase(String databaseName) throws RuntimeException;

    public void dropTable(String tableName) throws RuntimeException;

    public void insertIntoTable(String tableName, List<String> values) throws RuntimeException;

    public void insertIntoTable(String tableName, List<String> colNames, List<String> values)
            throws RuntimeException;

    public String selectFromTable(String tableName, List<String> colNames) throws RuntimeException;

    public void updateTable(String tableName, List<String> colNames, List<String> values)
            throws RuntimeException;

    public void useDatabase(String databaseName) throws RuntimeException;
}
