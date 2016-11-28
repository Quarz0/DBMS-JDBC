package controller;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Database;
import model.DatabaseFilterGenerator;
import model.DatabaseHelper;
import model.Observer;
import model.Record;
import model.Table;

public class DatabaseController implements DBMS, Observer {
    private DBMSController dbmsController;
    private DatabaseHelper dbHelper;
    private DatabaseFilterGenerator databaseFilter;

    public DatabaseController(DBMSController dbmsController) {
        databaseFilter = new DatabaseFilterGenerator();
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
    public boolean useDatabase(String databaseName) {
        File usedDatabase = getDatabase(databaseName);
        if (usedDatabase == null) {
            // throw new RuntimeException("Database doesnot exist");
            return false;
        }
        dbHelper.getCurrentDatabase().useDatabase(usedDatabase);
        reLoadTables(dbHelper.getCurrentDatabase());
        return true;
    }

    @Override
    public boolean createDatabase(String databaseName) {
        if (databaseExists(databaseName)) {
            return false;
        }
        new Database(dbHelper.getWorkSpacePath(), databaseName);
        return true;
    }

    @Override
    public boolean createTable(String tableName, List<String> colNames, List<Class<?>> types) {
        if (colNames.size() != types.size() || containsDublicates(colNames)) {
            throw new RuntimeException("Wrong data entered");
        }
        if (tableExists(tableName)) {
            throw new RuntimeException("Table already exists");
        }
        File tableFile = new File(
                dbHelper.getCurrentDatabase().getPath() + File.separator + tableName);
        Table table = new Table(tableFile);
        dbHelper.getCurrentDatabase().registerTable(table);
        File xmlFile = dbmsController.getXMLController().initializeXML(table.getTablePath(),
                tableName, colNames, types);
        File dtdFile = dbmsController.getXMLController().initializeDTD(table.getTablePath(),
                tableName, colNames, types);
        table.registerFiles(xmlFile, dtdFile);
        return true;
    }

    @Override
    public boolean dropTable(String tableName) {
        int index = 0;
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (table.getTableName().equals(tableName)) {
                deleteDir(table.getTableDir());
                dbHelper.getCurrentDatabase().getTables().remove(index);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dropDatabase(String databaseName) {
        File database = getDatabase(databaseName);
        if (database == null) {
            return false;
        }
        deleteDir(database);
        return true;
    }

    @Override
    public boolean insertIntoTable(String tableName, List<String> colNames, List<Object> values) {
        return false;
    }

    @Override
    public boolean insertIntoTable(String tableName, List<Object> values) {
        Record record = new Record(values);
        Table table = getTable(tableName);
        return dbmsController.getXMLController().insertIntoTable(table, record);
    }

    @Override
    public boolean selectFromTable(String tableName, List<String> colNames, String condition) {
        return false;
    }

    @Override
    public boolean updateTable(String tableName, List<String> colNames, List<Object> values,
            String condition) {
        return false;
    }

    @Override
    public boolean deleteFromTable(String tableName, List<String> colNames, String condition) {
        return false;
    }

    private Table getTable(String tableName) {
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (table.getTableName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }

    private <T> boolean containsDublicates(List<T> list) {
        Set<T> temp = new HashSet<>(list);
        if (temp.size() != list.size()) {
            return true;
        }
        return false;
    }

    private boolean databaseExists(String databaseName) {
        File database = getDatabase(databaseName);
        if (database == null) {
            return false;
        }
        return true;
    }

    private boolean tableExists(String tableName) {
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (table.getTableName().equals(tableName)) {
                return true;
            }
        }
        return false;
    }

    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    private File getDatabase(String databaseName) {
        File[] databases = dbHelper.getDatabaseDir().listFiles(databaseFilter);
        for (File databaseFile : databases) {
            if (databaseFile.getName().equals(databaseName)) {
                return databaseFile;
            }
        }
        return null;
    }

    private void reLoadTables(Database database) {
        database.clearTableList();
        File[] tables = database.getDatabaseDir().listFiles(databaseFilter);
        for (File tableDir : tables) {
            Table table = new Table(tableDir);
            File xmlFile = new File(
                    table.getTablePath() + File.separator + table.getTableName() + ".xml");
            File dtdFile = new File(
                    table.getTablePath() + File.separator + table.getTableName() + ".dtd");
            if (!(xmlFile.exists() && dtdFile.exists())) {
                throw new RuntimeException("Missing table files");
            }
            table.registerFiles(xmlFile, dtdFile);
            database.getTables().add(new Table(tableDir));
        }
    }
}
