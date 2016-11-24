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
        workSpaceDir = System.getProperty("user.home") + File.separator + "Workspace";
        workspace = new File(workSpaceDir);
        if (!workspace.mkdir()) {
            throw new RuntimeException("Cannot create workspace directory");
        }
        workSpaceDir += File.separator;
        this.dbController = databaseController;
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
    }

    public String getWorkSpaceDir() {
        return workSpaceDir;
    }

    public void addDatabase(Database database) {
        for (Database i : databases) {
            if (i.getDatabaseName().equals(database.getDatabaseName())) {
                throw new RuntimeException("Database already exists");
            }
        }
        databases.add(database);
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
