package model;

import java.io.File;

import controller.DatabaseController;

public class DatabaseHelper {
    private Database currentDatabase;
    private DatabaseController dbController;
    private SelectionTable selectedTable;
    private File workspaceDir;

    public DatabaseHelper(DatabaseController databaseController) {
        createAppPath();
        currentDatabase = new Database();
        this.dbController = databaseController;
    }

    private void createAppPath() {
        String workspace = System.getProperty("user.home") + File.separatorChar + "Workspace";
        workspaceDir = new File(workspace);
        if (!workspaceDir.exists()) {
            if (!workspaceDir.mkdir()) {
                throw new RuntimeException("Cannot create workspace directory");
            }
        }
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
    }

    public String getWorkSpacePath() {
        return workspaceDir.getAbsolutePath();
    }

    public File getDatabaseDir() {
        return workspaceDir;
    }

    public void setSelectedTable(SelectionTable selectedTable) {

        this.selectedTable = selectedTable;
    }

    public SelectionTable getSelectedTable() {
        return selectedTable;
    }

}