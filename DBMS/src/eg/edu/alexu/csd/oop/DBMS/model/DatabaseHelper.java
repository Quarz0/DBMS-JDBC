package eg.edu.alexu.csd.oop.DBMS.model;

import java.io.File;
import java.io.IOException;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMSController;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.BackEndWriter;
import eg.edu.alexu.csd.oop.DBMS.util.App;
import eg.edu.alexu.csd.oop.DBMS.util.RegexEvaluator;

public class DatabaseHelper {
    private BackEndWriter backEndWriter;
    private Database currentDatabase;
    private DatabaseFilterGenerator databaseFilter;
    @SuppressWarnings("unused")
    private DBMSController dbmsController;
    private SelectionTable selectedTable;
    private SelectionTable tempTable;
    private SelectionTable unionTable;
    private File workspaceDir;

    public DatabaseHelper(DBMSController dbmsController) {
        databaseFilter = new DatabaseFilterGenerator();
        currentDatabase = null;
        this.dbmsController = dbmsController;
    }

    public boolean databaseExists(String databaseName) {
        File databaseDir = getDatabase(databaseName);
        if (!App.checkForExistence(databaseDir)) {
            return false;
        }
        return true;
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

    private BackEndWriter fetchWriter(File tableDir) {
        File[] dataFiles = tableDir.listFiles();
        if (!App.checkForExistence(dataFiles))
            return null;
        for (File file : dataFiles) {
            if (file.getName().matches("[a-zA-Z_]\\w*\\.\\w+")) {
                String[] extension = RegexEvaluator.evaluate(file.getName(),
                        "[a-zA-Z_]\\w*\\.(\\w+)");
                if (App.checkForExistence(extension) && App
                        .checkForExistence(BackEndWriterFactory.getBackEndWriter(extension[1]))) {
                    return BackEndWriterFactory.getBackEndWriter(extension[1]);
                }
            }
        }
        return null;
    }

    public BackEndWriter getBackEndWriter() {
        return backEndWriter;
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
    }

    public File getDatabase(String databaseName) {
        File[] databases = this.getDatabaseDir().listFiles(databaseFilter);
        if (!App.checkForExistence(databases))
            return null;
        for (File databaseFile : databases) {
            if (databaseFile.getName().equals(databaseName)) {
                return databaseFile;
            }
        }
        return null;
    }

    public File getDatabaseDir() {
        return workspaceDir;
    }

    public SelectionTable getSelectedTable() {
        return selectedTable;
    }

    public Table getTable(String tableName) {
        for (Table table : this.getCurrentDatabase().getTables()) {
            if (table.getTableName().equalsIgnoreCase(tableName)) {
                return table;
            }
        }
        return null;
    }

    public SelectionTable getTempTable() {
        return tempTable;
    }

    public SelectionTable getUnionTable() {
        return unionTable;
    }

    public File getWorkspaceDir() {
        return workspaceDir;
    }

    public void loadTables(Database database) {
        database.clearTableList();
        File[] tables = database.getDatabaseDir().listFiles(databaseFilter);
        if (!App.checkForExistence(tables))
            throw new RuntimeException("No tables were found in " + database.getDatabaseName());
        for (File tableDir : tables) {
            BackEndWriter writer = this.fetchWriter(tableDir);
            if (App.checkForExistence(writer)) {
                Table table = new Table(tableDir);
                File dataFile = new File(table.getTablePath() + File.separator
                        + table.getTableName() + writer.getDataFileExtension());
                File validatorFile = new File(table.getTablePath() + File.separator
                        + table.getTableName() + writer.getValidatorFileExtension());
                if (!(dataFile.exists() && validatorFile.exists())) {
                    throw new RuntimeException("Table was hacked!");
                }
                table.registerFiles(dataFile, validatorFile);
                database.getTables().add(table);
            }
        }
    }

    public void readTable(String tableIdentifier) throws RuntimeException {
        SelectionTable selectionTable = null;
        if (!App.checkForExistence(this.getCurrentDatabase())) {
            throw new RuntimeException("Try using the \"USE\" statement first :)");
        }
        Table table = this.getTable(tableIdentifier);
        if (!App.checkForExistence(table)) {
            throw new RuntimeException("Unfortunately i do not have your table :(");
        }
        try {

            selectionTable = this.backEndWriter.readTable(table);
        } catch (IOException e) {
            throw new RuntimeException("Error while attempting to read from table");
        }
        try {
            this.tempTable = (SelectionTable) selectionTable.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Run, Forest, Run!");
        }
        this.selectedTable = selectionTable;
        this.selectedTable.setTableSchema(table);
    }

    public void requestClone() throws RuntimeException {
        try {
            this.tempTable = (SelectionTable) this.selectedTable.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Permission denied, Table cloning failed");
        }
    }

    public void resetTables() {
        this.selectedTable = null;
        this.unionTable = null;
        this.tempTable = null;
    }

    public void setBackEndWriter(BackEndWriter backEndWriter) {
        this.backEndWriter = backEndWriter;
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

    public boolean tableExists(String tableName) {
        for (Table table : this.getCurrentDatabase().getTables()) {
            if (table.getTableName().equals(tableName)) {
                return true;
            }
        }
        return false;
    }

}