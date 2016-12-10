package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import controller.backEnd.BackEndWriter;
import model.Database;
import model.DatabaseHelper;
import model.ObjectFactory;
import model.Observer;
import model.Record;
import model.SelectionTable;
import model.Table;
import model.statements.Viewable;
import model.statements.Writable;
import util.App;

public class DatabaseController implements DBMS, Observer {
    private DatabaseHelper dbHelper;
    private DBMSClause dbmsClause;
    private DBMSController dbmsController;
    private ObjectFactory objectFactory;

    public DatabaseController(DBMSController dbmsController) {
        this.objectFactory = new ObjectFactory();
        this.dbmsController = dbmsController;
        this.dbHelper = new DatabaseHelper(dbmsController);
        this.dbmsClause = new ClauseController(dbmsController);

    }

    @Override
    public void createDatabase(String databaseName) throws RuntimeException {
        if (this.dbHelper.databaseExists(databaseName)) {
            throw new RuntimeException();
        }
        new Database(this.dbHelper.getWorkspaceDir().getAbsolutePath(), databaseName);
    }

    @Override
    public void createTable(String tableName, Map<String, Class<?>> columns,
            BackEndWriter backEndWriter) throws RuntimeException {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())
                || this.dbHelper.tableExists(tableName)) {
            throw new RuntimeException();
        }
        File tableFile = new File(
                dbHelper.getCurrentDatabase().getPath() + File.separator + tableName);
        Table table = new Table(tableFile, backEndWriter);
        dbHelper.getCurrentDatabase().registerTable(table);
        table.registerFiles(
                table.getBackEndWriter().makeDataFile(table.getTablePath(), tableName, columns),
                table.getBackEndWriter().makeValidatorFile(table.getTablePath(), tableName,
                        columns));
    }

    @Override
    public void deleteFromTable(String tableName) throws RuntimeException {
        this.dbHelper.readTable(tableName);
        dbHelper.getSelectedTable().getRecordList().clear();
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
        throw new RuntimeException("Table not found!");
    }

    private SelectionTable formSelectionTable(SelectionTable selectedTable, String... colNames) {
        Map<String, Integer> tableColNamesLowerCase = new Hashtable<>();
        Map<String, Class<?>> header = new LinkedHashMap<>();
        int index = 0;
        for (Entry<String, Class<?>> entry : selectedTable.getHeader().entrySet()) {
            tableColNamesLowerCase.put(entry.getKey().toLowerCase(), index++);
            header.put(entry.getKey(), entry.getValue());
        }
        LinkedList<Integer> selectedColIndices = new LinkedList<Integer>();
        for (String colName : colNames) {
            selectedColIndices.add(tableColNamesLowerCase.get(colName));
        }
        SelectionTable result = new SelectionTable(selectedTable.getTableName(), header);
        for (Record record : selectedTable.getRecordList()) {
            Record newRecord = new Record(selectedTable.getHeader());
            ListIterator<Integer> it = selectedColIndices.listIterator();
            while (it.hasNext()) {
                newRecord.addToRecord(record.getValues().get(it.next()));
            }
            result.addRecord(newRecord);
        }
        return result;
    }

    public DatabaseHelper getHelper() {
        return dbHelper;
    }

    @Override
    public void insertIntoTable(String tableName, Map<String, String> columns)
            throws RuntimeException {
        dbHelper.readTable(tableName);
        Map<String, Class<?>> tableColumns = dbHelper.getSelectedTable().getHeader();
        Record record = new Record(tableColumns);
        int keysFound = 0;
        for (Entry<String, Class<?>> entry : tableColumns.entrySet()) {
            String colName = entry.getKey().toLowerCase();
            if (columns.containsKey(colName)) {
                try {
                    record.addToRecord(objectFactory.parseToObject(entry.getValue(),
                            columns.get(colName.trim()).trim()));
                } catch (Exception e) {
                    throw new RuntimeException("Wrong data");
                }
                keysFound++;
            } else {
                record.addToRecord(null);
            }
        }
        if (keysFound != columns.size()) {
            throw new RuntimeException("Wrong data!");
        }
        this.dbHelper.getSelectedTable().addRecord(record);
    }

    @Override
    public void insertIntoTable(String tableName, String... values) throws RuntimeException {
        this.dbHelper.readTable(tableName);
        Map<String, Class<?>> tableColumns = dbHelper.getSelectedTable().getHeader();
        if (values.length != tableColumns.size()) {
            throw new RuntimeException("Wrong data!");
        }
        int index = 0;
        Record record = new Record(tableColumns);
        for (Class<?> type : tableColumns.values()) {
            try {
                record.addToRecord(objectFactory.parseToObject(type, values[index++].trim()));
            } catch (Exception e) {
                throw new RuntimeException("Wrong data!");
            }
        }
        this.dbHelper.getSelectedTable().addRecord(record);
    }

    @Override
    public void selectAllFromTable(String tableName) throws RuntimeException {
        dbHelper.readTable(tableName);
    }

    @Override
    public void selectFromTable(String tableName, String... colNames) throws RuntimeException {
        dbHelper.readTable(tableName);
        dbHelper.setSelectedTable(formSelectionTable(dbHelper.getSelectedTable(), colNames));
    }

    @Override
    public void update() throws RuntimeException {
        this.dbmsController.getSQLParserController().getSqlParserHelper().getCurrentQuery()
                .execute(this);
        this.dbmsController.getSQLParserController().getSqlParserHelper().getCurrentQuery()
                .getClauses().forEach(clause -> clause.execute(this.dbmsClause));
        if (App.checkForExistence(this.dbHelper.getSelectedTable())) {
            try {
                if (this.dbmsController.getSQLParserController().getSqlParserHelper()
                        .getCurrentQuery() instanceof Writable)
                    this.dbHelper.getSelectedTable().getTableSchema().getBackEndWriter()
                            .writeTable(this.getHelper().getSelectedTable());
            } catch (FileNotFoundException e) {
                throw new RuntimeException();
            }

            if (this.dbmsController.getSQLParserController().getSqlParserHelper()
                    .getCurrentQuery() instanceof Viewable) {
                this.dbmsController.getCLIController()
                        .feedback(this.dbHelper.getSelectedTable().toString());

            }
        }

    }

    private void updateTable(SelectionTable selectedTable, Map<Integer, Object> values) {
        for (Record record : selectedTable.getRecordList()) {
            for (Entry<Integer, Object> entry : values.entrySet()) {
                record.getValues().set(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void updateTable(String tableName, Map<String, String> columns) throws RuntimeException {
        this.dbHelper.readTable(tableName);
        Map<String, Class<?>> tableColumns = dbHelper.getSelectedTable().getHeader();
        Map<Integer, Object> newValues = new Hashtable<>();
        int index = 0;
        for (Entry<String, Class<?>> entry : tableColumns.entrySet()) {
            String key = entry.getKey().toLowerCase();
            if (columns.containsKey(key)) {
                try {
                    newValues.put(index, objectFactory.parseToObject(entry.getValue(),
                            columns.get(key.trim()).trim()));
                } catch (Exception e) {
                    throw new RuntimeException("Wrong data");
                }
            }
            index++;
        }
        updateTable(dbHelper.getSelectedTable(), newValues);
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
