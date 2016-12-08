package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import model.ClassFactory;
import model.Database;
import model.DatabaseHelper;
import model.ObjectFactory;
import model.Observer;
import model.Record;
import model.SelectionTable;
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

    // public boolean evaluate(String expression, Record record) throws ScriptException {
    // String exp = getFilledExpression(expression, record);
    // exp = exp.toLowerCase();
    // exp = App.replace(exp, "and", " && ");
    // exp = App.replace(exp, "or", " || ");
    // exp = App.replace(exp, "not", " ! ");
    // return BooleanEvaluator.evaluate(exp);
    // }

    // private String getFilledExpression(String expression, Record record) {
    // String exp = expression.toLowerCase();
    // for (int i = 0; i < record.getColumns().size(); i++) {
    // if (exp.charAt(0) == '\"') {
    // exp = App.replace(exp, record.getColumns().get(i).toLowerCase(),
    // "\"" + record.getValues().get(i).toString() + "\"");
    // } else {
    // exp = App.replace(exp, record.getColumns().get(i).toLowerCase(),
    // record.getValues().get(i).toString());
    // }
    // }
    // return exp;
    // }

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
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("Database not selected!");
        }
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

    @Override
    public void insertIntoTable(String tableName, String... values) throws RuntimeException {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("Database not selected!");
        }
        dbHelper.readTable(tableName);
        Map<String, Class<?>> tableColumns = dbHelper.getSelectedTable().getHeader();
        if (values.length != tableColumns.size()) {
            throw new RuntimeException("Wrong data!");
        }
        int index = 0;
        Record record = new Record();
        for (Class<?> type : tableColumns.values()) {
            try {
                record.addToRecord(objectFactory.parseToObject(type, values[index++]));
            } catch (Exception e) {
                throw new RuntimeException("Wrong data!");
            }
        }
        dbHelper.getSelectedTable().addRecord(record);
    }

    @Override
    public void insertIntoTable(String tableName, Map<String, String> columns)
            throws RuntimeException {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("Database not selected!");
        }
        dbHelper.readTable(tableName);
        Map<String, Class<?>> tableColumns = dbHelper.getSelectedTable().getHeader();
        Record record = new Record();
        int keysFound = 0;
        for (Entry<String, Class<?>> entry : tableColumns.entrySet()) {
            String colName = entry.getKey().toLowerCase();
            if (columns.containsKey(colName)) {
                try {
                    record.addToRecord(
                            objectFactory.parseToObject(entry.getValue(), columns.get(colName)));
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
        dbHelper.getSelectedTable().addRecord(record);
    }

    @Override
    public void selectFromTable(String tableName, String... colNames) throws RuntimeException {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("Database not selected!");
        }
        dbHelper.readTable(tableName);
        dbHelper.setSelectedTable(formSelectionTable(dbHelper.getSelectedTable(), colNames));
    }

    @Override
    public void selectAllFromTable(String tableName) throws RuntimeException {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("Database not selected!");
        }
        dbHelper.readTable(tableName);
    }

    @Override
    public void updateTable(String tableName, Map<String, String> columns) throws RuntimeException {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("Database not selected!");
        }
        dbHelper.readTable(tableName);
        Map<String, Class<?>> tableColumns = dbHelper.getSelectedTable().getHeader();
        Map<Integer, Object> newValues = new Hashtable<>();
        int index = 0;
        for (Entry<String, Class<?>> entry : tableColumns.entrySet()) {
            String key = entry.getKey().toLowerCase();
            if (columns.containsKey(key)) {
                try {
                    newValues.put(index,
                            objectFactory.parseToObject(entry.getValue(), columns.get(key)));
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
            Record newRecord = new Record();
            ListIterator<Integer> it = selectedColIndices.listIterator();
            while (it.hasNext()) {
                newRecord.addToRecord(record.getValues().get(it.next()));
            }
            result.addRecord(newRecord);
        }
        return result;
    }

    private void updateTable(SelectionTable selectedTable, Map<Integer, Object> values) {
        for (Record record : selectedTable.getRecordList()) {
            for (Entry<Integer, Object> entry : values.entrySet()) {
                record.getValues().set(entry.getKey(), entry.getValue());
            }
        }
    }
}
