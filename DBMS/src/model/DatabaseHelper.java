package model;

import java.io.File;

import controller.DatabaseController;

public class DatabaseHelper {
    private Database currentDatabase;
    private DatabaseController dbController;
    private SelectionTable selectedTable;
    private File workspaceDir;

    public DatabaseHelper(DatabaseController databaseController) {
        currentDatabase = null;
        this.dbController = databaseController;
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
    }

    public File getDatabaseDir() {
        return workspaceDir;
    }

    public SelectionTable getSelectedTable() {
        return selectedTable;
    }

    public File getWorkspaceDir() {
        return workspaceDir;
    }

    public void setDatabase(File usedDatabaseDir) {

        currentDatabase = new Database();
        currentDatabase.useDatabase(usedDatabaseDir);
    }

    public void setSelectedTable(SelectionTable selectedTable) {

        this.selectedTable = selectedTable;
    }

    public void setWorkspaceDir(File workspaceDir) {
        this.workspaceDir = workspaceDir;
    }
}