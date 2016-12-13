package controller;

import java.util.Map;

import controller.backEnd.BackEndWriter;

public interface DBMS {

    public void alterTableAdd(String tableName, String columnIdentifier, Class<?> type)
            throws RuntimeException;

    public void alterTableDrop(String tableName, String columnIdentifier) throws RuntimeException;

    public void alterTableModify(String tableName, String columnIdentifier, Class<?> type)
            throws RuntimeException;

    public void createDatabase(String databaseName, BackEndWriter backEndWriter)
            throws RuntimeException;

    public void createTable(String tableName, Map<String, Class<?>> columns)
            throws RuntimeException;

    public void deleteFromTable(String tableName) throws RuntimeException;

    public void dropDatabase(String databaseName) throws RuntimeException;

    public void dropTable(String tableName) throws RuntimeException;

    public void insertIntoTable(String tableName, Map<String, String> columns)
            throws RuntimeException;

    public void insertIntoTable(String tableName, String... values) throws RuntimeException;

    public void selectAllFromTable(String tableName) throws RuntimeException;

    public void selectFromTable(String tableName, String... colNames) throws RuntimeException;

    public void updateTable(String tableName, Map<String, String> columns) throws RuntimeException;

    public void useDatabase(String databaseName) throws RuntimeException;
}
