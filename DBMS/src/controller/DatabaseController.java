package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Database;
import model.DatabaseHelper;
import model.Observer;
import model.Table;
import model.statements.Clause;

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
    public boolean createDatabase(String databaseName) {
        for (Database x : dbHelper.getDatabases()) {
            if (x.getDatabaseName().equals(databaseName)) {
                return false;
            }
        }
        dbHelper.getDatabases().add(new Database(dbHelper.getWorkSpaceDir(), databaseName));
        return true;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean createTable(String tableName, List<String> colNames, List<Class<?>> types) {
        if (colNames.size() != types.size()) {
            return false;
        }
        for (Table x : dbHelper.getCurrentDatabase().getTables()) {
            if (x.getTableName().equals(tableName)) {
                return false;
            }
        }
        dbHelper.getCurrentDatabase().getTables().add(new Table(tableName,
                dbHelper.getCurrentDatabase().getDirectory(), colNames, types));
        return true;
    }

    @Override
    public boolean dropTable(String tableName) {
        for (Table x : dbHelper.getCurrentDatabase().getTables()) {
            if (x.getTableName().equals(tableName)) {
                deleteFile(x.getTableFile());
                dbHelper.getCurrentDatabase().getTables().remove(x);
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
                dbHelper.getDatabases().remove(x);
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

    @Override
    public boolean insertIntoTable(String tableName, List<String> colNames, List<Object> values) {
        if (colNames.size() != values.size() || containsDublicates(colNames))
            return false;
        List<Integer> colIndex = new ArrayList<>();
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (table.getTableName().equals(tableName)) {
                for (int i = 0; i < colNames.size(); i++) {
                    int temp = table.containsColumn(colNames.get(i));
                    if (temp == -1 || !table.getColumnType(temp).isInstance(values.get(i))) {
                        return false;
                    }
                    colIndex.add(temp);
                }
                for (int i = 0; i < colNames.size(); i++)
                    table.getColumn(colIndex.get(i)).addData(values.get(i));
            }
        }
        return false;
    }

    private <T> boolean containsDublicates(List<T> list) {
        Set<T> temp = new HashSet<>(list);
        if (temp.size() != list.size()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean insertIntoTable(String tableName, List<Object> values) {
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (table.getTableName().equals(tableName)) {
                if (table.getColumnsList().size() == values.size()) {
                    for (int i = 0; i < values.size(); i++) {
                        if (!table.getColumn(i).getType().isInstance(values.get(i))) {
                            return false;
                        }
                    }
                    for (int i = 0; i < values.size(); i++) {
                        table.getColumn(i).addData(values.get(i));
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean selectFromTable(String tableName, List<String> colNames, List<Clause> clauses) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateTable(String tableName, List<String> colNames, List<Object> values,
            List<Clause> clauses) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deleteFromTable(String tableName, List<String> colNames, List<Object> values,
            List<Clause> clauses) {
        // TODO Auto-generated method stub
        return false;
    }

}
