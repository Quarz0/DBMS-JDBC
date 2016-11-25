package controller;

import java.util.List;

import model.DatabaseHelper;
import model.statements.Query;

public class DatabaseController implements DBMS {
    private DBMSController dbmsController;
    private DatabaseHelper dbHelper;

    public DatabaseController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        dbHelper = new DatabaseHelper(this);
    }

    public DatabaseHelper getHelper() {
        return dbHelper;
    }

    @Override
    public boolean createDatabase(String databaseName) {
        return false;
    }

    @Override
    public boolean createTable(String tableName, List<String> colNames, List<Class<?>> types) {
        return false;
    }

    @Override
    public boolean dropTable(String tableName) {
        return false;
    }

    @Override
    public boolean dropDatabase(String databaseName) {
        return false;
    }

    @Override
    public boolean insertIntoTable(String tableName, List<String> colNames, List<Object> values) {
        return false;
    }

    @Override
    public boolean insertIntoTable(String tableName, List<Object> values) {
        return false;
    }

    @Override
    public boolean updateTable(String tableName, List<String> colNames, List<Object> values) {
        return false;
    }

    @Override
    public boolean selectFromTable(String tableName, List<String> colNames) {
        return false;
    }

    @Override
    public boolean deleteFromTable(String tableName, List<String> colNames, List<Object> values) {
        return false;
    }

}
