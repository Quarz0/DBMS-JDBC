package util;

import java.util.HashSet;

import model.Database;

public class DatabaseHelper {
    private HashSet<Database> databases;
    private Database currentDatabase;

    public DatabaseHelper() {
        databases = new HashSet<>();
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
    }

    public void setCurrentDatabase(Database currentDatabase) {
        this.currentDatabase = currentDatabase;
    }
    
}
