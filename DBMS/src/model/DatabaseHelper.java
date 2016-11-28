package model;

import java.io.File;
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
