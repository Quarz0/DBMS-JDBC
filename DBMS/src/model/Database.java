package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private String databaseName;
    private List<Table> tables;
    private File databaseDir;

    public Database(String workspaceDirc, String databaseName) {
        createDatabaseDir(workspaceDirc + File.separator + databaseName);
        tables = new ArrayList<>();
        this.databaseName = databaseName;
    }

    public Database() {
        tables = new ArrayList<>();
    }

    public void clearTableList() {
        tables.clear();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public File getDatabaseDir() {
        return databaseDir;
    }

    public String getPath() {
        return databaseDir.getAbsolutePath();
    }

    public void registerTable(Table table) {
        tables.add(table);
    }

    public void dropTable(Table table) {
        tables.remove(table);
    }

    public void useDatabase(File databaseDir) {
        this.databaseDir = databaseDir;
        databaseName = databaseDir.getName();
    }

    public List<Table> getTables() {
        return tables;
    }

    private void createDatabaseDir(String databasePath) {
        databaseDir = new File(databasePath);
        if (!databaseDir.mkdir()) {
            throw new RuntimeException("Cannot create database directory");
        }
    }
}
