package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private File databaseDir;
    private String databaseName;
    private List<Table> tables;

    public Database() {
        tables = new ArrayList<>();
    }

    public Database(String workspaceDirc, String databaseName) {
        createDatabaseDir(workspaceDirc + File.separator + databaseName);
        tables = new ArrayList<>();
        this.databaseName = databaseName;
    }

    public void clearTableList() {
        tables.clear();
    }

    private void createDatabaseDir(String databasePath) {
        databaseDir = new File(databasePath);
        if (!databaseDir.mkdir()) {
            throw new RuntimeException("Cannot create database directory");
        }
    }

    public void dropTable(Table table) {
        tables.remove(table);
    }

    public File getDatabaseDir() {
        return databaseDir;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getPath() {
        return databaseDir.getAbsolutePath();
    }

    public List<Table> getTables() {
        return tables;
    }

    public void registerTable(Table table) {
        tables.add(table);
    }

    public void useDatabase(File databaseDir) {
        this.databaseDir = databaseDir;
        databaseName = databaseDir.getName();
    }
}
