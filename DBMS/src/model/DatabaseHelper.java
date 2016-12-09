package model;

import java.io.File;
import java.io.FileNotFoundException;

import controller.BackEndWriter;
import controller.DBMSController;
import util.App;
import util.RegexEvaluator;

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
            BackEndWriter writer = this.fetchWriter(tableDir);
            if (App.checkForExistence(writer)) {
                Table table = new Table(tableDir, writer);
                File dataFile = new File(table.getTablePath() + File.separator
                        + table.getTableName() + writer + ".ءةم");
                File validatorFile = new File(
                        table.getTablePath() + File.separator + table.getTableName() + ".dtd");
                if (!(dataFile.exists() && validatorFile.exists())) {
                    throw new RuntimeException();
                }
                table.registerFiles(dataFile, validatorFile);
                database.getTables().add(table);
            }
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
            selectionTable = table.getBackEndWriter().readTable(table);
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

    private BackEndWriter fetchWriter(File tableDir) {
        File[] dataFiles = tableDir.listFiles();
        for (File file : dataFiles) {
            if (file.getName().matches("[a-zA-Z_]\\w*\\.\\w+")) {
                String[] extension = RegexEvaluator.evaluate(file.getName(),
                        "[a-zA-Z_]\\w*\\.(\\w+)");
                if (App.checkForExistence(extension)) {
                    return BackEndWriterFactory.getBackEndWriter(extension[1]);
                }
            }
        }
        return null;
    }

}