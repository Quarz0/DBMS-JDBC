package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Database;
import model.DatabaseHelper;
import model.Observer;
import model.Record;
import model.Table;

public class DatabaseController implements DBMS, Observer {
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
    public void update() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean createDatabase(String databaseName) {
        if (databaseExists(databaseName)) {
            return false;
        }
        dbHelper.registerDatabase(new Database(dbHelper.getWorkSpaceDir(), databaseName));
        return true;
    }

    @Override
    public boolean createTable(String tableName, List<String> colNames, List<Class<?>> types) {
        if (colNames.size() != types.size() || containsDublicates(colNames)
                || tablesExists(tableName)) {
            return false;
        }
        dbHelper.getCurrentDatabase().registerTable(new Table(tableName,
                dbHelper.getCurrentDatabase().getDirectory(), colNames, types));
        return true;
    }

    @Override
    public boolean dropTable(String tableName) {
        for (Table x : dbHelper.getCurrentDatabase().getTables()) {
            if (x.getTableName().equals(tableName)) {
                deleteFile(x.getTableFile());
                dbHelper.getCurrentDatabase().dropTable(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dropDatabase(String databaseName) {
        for (Database x : dbHelper.getDatabases()) {
            if (x.getDatabaseName().equals(databaseName)) {
                deleteFile(x.getDatabaseFile());
                dbHelper.dropDatabase(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean insertIntoTable(String tableName, List<String> colNames, List<Object> values) {
        if (colNames.size() != values.size() || containsDublicates(colNames)) {
            return false;
        }
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (table.getTableName().equals(tableName)) {
                if (!columnsExists(table, colNames)) {
                    return false;
                }
                table.addRecord(createNewRecord(table, colNames, values));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean insertIntoTable(String tableName, List<Object> values) {
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (table.getTableName().equals(tableName)) {
                if (!oneToOneColumnTypes(table, values)) {
                    return false;
                }
                table.addRecord(new Record(values));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean selectFromTable(String tableName, List<String> colNames, List<String> clauses) {
        return false;
    }

    @Override
    public boolean updateTable(String tableName, List<String> colNames, List<Object> values,
            List<String> clauses) {
        return false;
    }

    @Override
    public boolean deleteFromTable(String tableName, List<String> colNames, List<String> clauses) {
        return false;
    }

    private boolean oneToOneColumnTypes(Table table, List<Object> values) {
        if (table.getHeader().size() != values.size()) {
            return false;
        }
        for (int i = 0; i < values.size(); i++) {
            if (!table.getTypes().get(i).isInstance(values.get(i))) {
                return false;
            }
        }
        return true;
    }

    private Record createNewRecord(Table table, List<String> colNames, List<Object> values) {
        List<Object> record = new ArrayList<>();
        int it = 0;
        for (String name : table.getHeader()) {
            if (name.equals(colNames.get(it))) {
                record.add(values.get(it++));
            } else {
                record.add(null);
            }
        }
        return new Record(record);
    }

    private boolean columnsExists(Table table, List<String> colNames) {
        for (String name : colNames) {
            if (!table.getHeader().contains(name)) {
                return false;
            }
        }
        return true;
    }

    private <T> boolean containsDublicates(List<T> list) {
        Set<T> temp = new HashSet<>(list);
        if (temp.size() != list.size()) {
            return true;
        }
        return false;
    }

    private boolean databaseExists(String databaseName) {
        for (Database x : dbHelper.getDatabases()) {
            if (x.getDatabaseName().equals(databaseName)) {
                return true;
            }
        }
        return false;
    }

    private boolean tablesExists(String tableName) {
        for (Table x : dbHelper.getCurrentDatabase().getTables()) {
            if (x.getTableName().equals(tableName)) {
                return true;
            }
        }
        return false;
    }

    private void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File sub : file.listFiles()) {
                deleteFile(sub);
            }
        }
        file.delete();
    }
}
