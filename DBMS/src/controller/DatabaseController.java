package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import model.ClassFactory;
import model.Database;
import model.DatabaseHelper;
import model.ObjectFactory;
import model.Observer;
import model.Table;
import util.App;

public class DatabaseController implements DBMS, Observer {
    private ClassFactory classFactory;
    private DatabaseHelper dbHelper;
    private DBMSController dbmsController;
    private ObjectFactory objectFactory;
    private DBMSClause dbmsClause;

    public DatabaseController(DBMSController dbmsController) {
        classFactory = new ClassFactory();
        objectFactory = new ObjectFactory();
        this.dbmsController = dbmsController;
        this.dbHelper = new DatabaseHelper(dbmsController);
        this.dbmsClause = new ClauseController(dbmsController);

    }

    public DatabaseHelper getHelper() {
        return dbHelper;
    }

    @Override
    public void update() {
        this.dbmsController.getSQLParserController().getSqlParserHelper().getCurrentQuery()
                .execute(this);
        this.dbmsController.getSQLParserController().getSqlParserHelper().getCurrentQuery()
                .getClauses().forEach(clause -> clause.execute(this.dbmsClause));
    }

    @Override
    public void createDatabase(String databaseName) throws RuntimeException {
        if (this.dbHelper.databaseExists(databaseName)) {
            throw new RuntimeException();
        }
        new Database(this.dbHelper.getWorkspaceDir().getAbsolutePath(), databaseName);
    }

    @Override
    public void createTable(String tableName, Map<String, Class<?>> columns)
            throws RuntimeException {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())
                || this.dbHelper.tableExists(tableName)) {
            throw new RuntimeException();
        }
        File tableFile = new File(
                dbHelper.getCurrentDatabase().getPath() + File.separator + tableName);
        Table table = new Table(tableFile);
        dbHelper.getCurrentDatabase().registerTable(table);
        File xmlFile = dbmsController.getXMLController().initializeXML(table.getTablePath(),
                tableName, new ArrayList<>(columns.keySet()), new ArrayList<>(columns.values()));
        File dtdFile = dbmsController.getXMLController().initializeDTD(table.getTablePath(),
                tableName, new ArrayList<>(columns.keySet()), new ArrayList<>(columns.values()));
        table.registerFiles(xmlFile, dtdFile);
    }

    @Override
    public void deleteFromTable(String tableName) throws RuntimeException {
        this.dbHelper.readTable(tableName);
        
    }

    @Override
    public void dropDatabase(String databaseName) throws RuntimeException {
        File database = this.dbHelper.getDatabase(databaseName);
        if (!App.checkForExistence(database)) {
            throw new RuntimeException();
        }
        this.dbHelper.deleteDir(database);
    }

    @Override
    public void dropTable(String tableName) throws RuntimeException {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException();
        }
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (App.equalStrings(table.getTableName(), tableName)) {
                this.dbHelper.deleteDir(table.getTableDir());
                this.dbHelper.getCurrentDatabase().getTables().remove(table);
                return;
            }
        }
        throw new RuntimeException();
    }

    @Override
    public void insertIntoTable(String tableName, String... values) throws RuntimeException {
        // TODO Auto-generated method stub

    }

    @Override
    public void insertIntoTable(String tableName, Map<String, String> columns)
            throws RuntimeException {
        // TODO Auto-generated method stub

    }

    @Override
    public void selectFromTable(String tableName, String... colNames) throws RuntimeException {
        // TODO Auto-generated method stub

    }

    @Override
    public void selectAllFromTable(String tableName) throws RuntimeException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateTable(String tableName, Map<String, String> columns) throws RuntimeException {
        // TODO Auto-generated method stub

    }

    @Override
    public void useDatabase(String databaseName) throws RuntimeException {
        File usedDatabaseDir = this.dbHelper.getDatabase(databaseName);
        if (!App.checkForExistence(usedDatabaseDir)) {
            throw new RuntimeException();
        }
        this.dbHelper.setDatabase(usedDatabaseDir);
        this.dbHelper.loadTables(dbHelper.getCurrentDatabase());
    }

}
