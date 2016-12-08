package model;

import java.io.File;
import java.io.FileNotFoundException;

import controller.DBMSController;
import util.App;

public class DatabaseHelper {
    private Database currentDatabase;
    private DBMSController dbmsController;
    private SelectionTable selectedTable;
    private SelectionTable tempTable;
    private File workspaceDir;
    private DatabaseFilterGenerator databaseFilter;

    public DatabaseHelper(DBMSController dbmsController) {
        databaseFilter = new DatabaseFilterGenerator();
        currentDatabase = null;
        this.dbmsController = dbmsController;
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

    public SelectionTable getTempTable() {
        return tempTable;
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

    public boolean databaseExists(String databaseName) {
        File databaseDir = getDatabase(databaseName);
        if (!App.checkForExistence(databaseDir)) {
            return false;
        }
        return true;
    }

    public File getDatabase(String databaseName) {
        File[] databases = this.getDatabaseDir().listFiles(databaseFilter);
        for (File databaseFile : databases) {
            if (App.equalStrings(databaseFile.getName(), databaseName)) {
                return databaseFile;
            }
        }
        return null;
    }

    public void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (App.checkForExistence(contents)) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    public void loadTables(Database database) {
        database.clearTableList();
        File[] tables = database.getDatabaseDir().listFiles(databaseFilter);
        for (File tableDir : tables) {
            Table table = new Table(tableDir);
            File xmlFile = new File(
                    table.getTablePath() + File.separator + table.getTableName() + ".xml");
            File dtdFile = new File(
                    table.getTablePath() + File.separator + table.getTableName() + ".dtd");
            if (!(xmlFile.exists() && dtdFile.exists())) {
                throw new RuntimeException();
            }
            table.registerFiles(xmlFile, dtdFile);
            database.getTables().add(table);
        }
    }

    public boolean tableExists(String tableName) {
        for (Table table : this.getCurrentDatabase().getTables()) {
            if (App.equalStrings(table.getTableName(), tableName)) {
                return true;
            }
        }
        return false;
    }

    public Table getTable(String tableName) {
        for (Table table : this.getCurrentDatabase().getTables()) {
            if (App.equalStrings(table.getTableName(), tableName)) {
                return table;
            }
        }
        return null;
    }

    public Table readTable(String tableIdentifier) throws RuntimeException {
        SelectionTable selectionTable = null;
        if (!App.checkForExistence(this.getCurrentDatabase())) {
            throw new RuntimeException();
        }
        Table table = this.getTable(tableIdentifier);
        if (!App.checkForExistence(table)) {
            throw new RuntimeException();
        }
        try {
            selectionTable = dbmsController.getXMLController().readTable(table);
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
        try {
            this.tempTable = (SelectionTable) selectionTable.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        this.selectedTable = selectionTable;
        return table;
    }

}