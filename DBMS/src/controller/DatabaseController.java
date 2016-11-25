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
    public boolean create(String databaseName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean create(String tableName, List<String> colNames, List<Class<?>> types) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean dropTable(String tableName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean dropDatabase(String databaseName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean insertIntoTable(String tableName, List<String> colNames, List<Object> values) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean insertIntoTable(String tableName, List<Object> values) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateTable(String tableName, List<String> colNames, List<Object> values) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean selectFromTable(String tableName, List<String> colNames) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deleteFromTable(String tableName, List<String> colNames, List<Object> values) {
        // TODO Auto-generated method stub
        return false;
    }

}
