package model;

import java.io.File;
import java.util.List;

public class Database {
    private String databaseName;
    private List<Table> tables;
    private String databaseDir;
    private File databaseFile;

    public Database(String databaseName) {
//        this.databaseDir = databaseDirc + databaseName;
        createDatabaseDir();
        this.databaseName = databaseName;
    }

    private void createDatabaseDir() {
        databaseFile = new File(databaseDir);
        if (!databaseFile.mkdir()) {
            throw new RuntimeException("Cannot create database directory");
        }
        databaseDir += File.separator;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDirectory() {
        return databaseDir;
    }

    public List<Table> getTables() {
        return tables;
    }
}
