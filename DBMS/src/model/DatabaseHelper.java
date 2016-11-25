package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import controller.DatabaseController;

public class DatabaseHelper {
    private List<Database> databases;
    private Database currentDatabase;
    private DatabaseController dbController;
    private String workSpaceDir;
    private File workspace;

    public DatabaseHelper(DatabaseController databaseController) {
        databases = new ArrayList<>();
        createAppPath();
        this.dbController = databaseController;
    }

    private void createAppPath() {
        workSpaceDir = System.getProperty("user.home") + File.separator + "Workspace";
        workspace = new File(workSpaceDir);
        if (!workspace.mkdir()) {
            throw new RuntimeException("Cannot create workspace directory");
        }
        workSpaceDir += File.separator;
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
    }

    public List<Database> getDatabases() {
        return databases;
    }

    public String getWorkSpaceDir() {
        return workSpaceDir;
    }

    public void useDatabase(String databaseName) {
        for (Database i : databases) {
            if (i.getDatabaseName().equals(databaseName)) {
                currentDatabase = i;
                return;
            }
        }
        throw new RuntimeException("Database doesnot exists");
    }
}
