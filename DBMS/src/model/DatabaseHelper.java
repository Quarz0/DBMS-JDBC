package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import controller.DatabaseController;

public class DatabaseHelper {
    private Database currentDatabase;
    private DatabaseController dbController;
    private File workspace;

    public DatabaseHelper(DatabaseController databaseController) {
        this.dbController = databaseController;
        this.currentDatabase = null;
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
    }

    public void setWorkspace(File workspace) {
        this.workspace = workspace;
    }
}
