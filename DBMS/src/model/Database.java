package model;

import java.util.List;

public class Database {
    private String databaseName;
    private List<Table> tables;
    private String databaseDirc;

    public Database(String databaseDirc, String databaseName) {
        this.databaseDirc = databaseDirc;
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public List<Table> getTables() {
        return tables;
    }
}
