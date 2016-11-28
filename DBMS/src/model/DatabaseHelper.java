package model;

import java.io.File;

import controller.DatabaseController;

public class DatabaseHelper {
    private Database currentDatabase;
    private DatabaseController dbController;
    private SelectionTable selectedTable;
    private File workspaceDir;

    public DatabaseHelper(DatabaseController databaseController) {
        currentDatabase = new Database();
        this.dbController = databaseController;
        this.currentDatabase = null;
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
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

    public File getWorkspaceDir() {
        return workspaceDir;
    }

    public void setWorkspaceDir(File workspaceDir) {
        this.workspaceDir = workspaceDir;
    }

}