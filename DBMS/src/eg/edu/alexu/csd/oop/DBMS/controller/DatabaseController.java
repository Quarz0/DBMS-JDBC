package eg.edu.alexu.csd.oop.DBMS.controller;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import eg.edu.alexu.csd.oop.DBMS.model.Database;
import eg.edu.alexu.csd.oop.DBMS.model.DatabaseHelper;
import eg.edu.alexu.csd.oop.DBMS.model.Observer;
import eg.edu.alexu.csd.oop.DBMS.model.Record;
import eg.edu.alexu.csd.oop.DBMS.model.SelectionTable;
import eg.edu.alexu.csd.oop.DBMS.model.Table;
import eg.edu.alexu.csd.oop.DBMS.model.TypeFactory;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Writable;
import eg.edu.alexu.csd.oop.DBMS.util.App;

public class DatabaseController implements DBMS, Observer {
    private DatabaseHelper dbHelper;
    private DBMSClause dbmsClause;
    private DBMSController dbmsController;

    public DatabaseController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        this.dbHelper = new DatabaseHelper(dbmsController);
        this.dbmsClause = new ClauseController(dbmsController);
    }

    @Override
    public void alterTableAdd(String tableName, String colName, Class<?> type)
            throws RuntimeException {

        this.dbHelper.readTable(tableName);

        if (dbHelper.getSelectedTable().getDefaultHeader().containsKey(colName.toLowerCase())) {
            throw new RuntimeException("Column already exists!");
        }
        dbHelper.getSelectedTable().getHeader().put(colName, type);
        dbHelper.getSelectedTable().getDefaultHeader().put(colName.toLowerCase(), type);
        for (Record record : dbHelper.getSelectedTable().getRecordList()) {
            record.addToRecord(null);
        }
    }

    @Override
    public void alterTableDrop(String tableName, String colName) throws RuntimeException {

        this.dbHelper.readTable(tableName);
        if (!dbHelper.getSelectedTable().getDefaultHeader().containsKey(colName.toLowerCase())) {
            throw new RuntimeException("Column does not exist!");
        }
        dbHelper.getSelectedTable().getDefaultHeader().remove(colName.toLowerCase());
        int index = 0;
        String realColName = null;
        for (String str : dbHelper.getSelectedTable().getHeader().keySet()) {
            if (str.equalsIgnoreCase(colName)) {
                realColName = str;
                dbHelper.getSelectedTable().getHeader().remove(realColName);
                break;
            }
            index++;
        }
        for (Record record : dbHelper.getSelectedTable().getRecordList()) {
            record.getColumns().remove(realColName);
            record.getValues().remove(index);
        }
    }

    @Override
    public void alterTableModify(String tableName, String colName, Class<?> type)
            throws RuntimeException {

        this.dbHelper.readTable(tableName);
        if (!dbHelper.getSelectedTable().getDefaultHeader().containsKey(colName.toLowerCase())) {
            throw new RuntimeException("Column deos not exist!");
        }
        dbHelper.getSelectedTable().getDefaultHeader().put(colName.toLowerCase(), type);
        int index = 0;
        String realColName = null;
        for (String str : dbHelper.getSelectedTable().getHeader().keySet()) {
            if (str.equalsIgnoreCase(colName)) {
                realColName = str;
                dbHelper.getSelectedTable().getHeader().put(realColName, type);
                break;
            }
            index++;
        }
        for (Record record : dbHelper.getSelectedTable().getRecordList()) {
            record.getColumns().put(realColName, type);
            record.getValues().set(index, null);
        }
    }

    @Override
    public void createDatabase(String databaseName) throws RuntimeException {
        if (this.dbHelper.databaseExists(databaseName)) {
            throw new RuntimeException("Database already exists!");
        }
        new Database(this.dbHelper.getWorkspaceDir().getAbsolutePath(), databaseName);
    }

    @Override
    public void createTable(String tableName, Map<String, Class<?>> columns)
            throws RuntimeException {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException(
                    "Do you really expect me to figure out the database on my own?!");
        }
        if (this.dbHelper.tableExists(tableName)) {
            throw new RuntimeException("Table already exists!");
        }
        File tableFile = new File(
                dbHelper.getCurrentDatabase().getPath() + File.separator + tableName);
        Table table = new Table(tableFile);
        dbHelper.getCurrentDatabase().registerTable(table);
        try {
            table.registerFiles(
                    this.dbHelper.getBackEndWriter().makeDataFile(table.getTablePath(), tableName,
                            columns),
                    this.dbHelper.getBackEndWriter().makeValidatorFile(table.getTablePath(),
                            tableName, columns));
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException("Error while attempting to create table");
        }
    }

    @Override
    public void deleteFromTable(String tableName) throws RuntimeException {
        this.dbHelper.readTable(tableName);
        this.getHelper().getSelectedTable().setAffectedRecordsToSize();
        this.dbHelper.getSelectedTable().getRecordList().clear();
    }

    @Override
    public void dropDatabase(String databaseName) throws RuntimeException {
        File database = this.dbHelper.getDatabase(databaseName);
        if (!App.checkForExistence(database)) {
            throw new RuntimeException("Database does not exist!");
        }
        this.dbHelper.deleteDir(database);
    }

    @Override
    public void dropTable(String tableName) throws RuntimeException {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("Database does not exist!");
        }
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (table.getTableName().equals(tableName)) {
                this.dbHelper.deleteDir(table.getTableDir());
                this.dbHelper.getCurrentDatabase().getTables().remove(table);
                return;
            }
        }
        throw new RuntimeException("Table does not exist!");
    }

    private SelectionTable formSelectionTable(SelectionTable selectedTable, String... colNames) {
        Map<String, Integer> tableColNamesLowerCase = new Hashtable<>();
        Map<String, Class<?>> header = new LinkedHashMap<>();
        int index = 0;
        for (Entry<String, Class<?>> entry : selectedTable.getHeader().entrySet()) {
            tableColNamesLowerCase.put(entry.getKey().toLowerCase(), index++);
        }
        for (String colName : colNames) {
            header.put(colName,
                    dbHelper.getSelectedTable().getDefaultHeader().get(colName.toLowerCase()));
        }
        LinkedList<Integer> selectedColIndices = new LinkedList<Integer>();
        for (String colName : colNames) {
            selectedColIndices.add(tableColNamesLowerCase.get(colName.toLowerCase()));
        }
        SelectionTable result = new SelectionTable(selectedTable.getTableName(), header);
        for (Record record : selectedTable.getRecordList()) {
            Record newRecord = new Record(header);
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
                    record.addToRecord(TypeFactory.parseToObject(entry.getValue(),
                            columns.get(colName.trim())));
                } catch (ClassCastException e) {
                    throw new RuntimeException(e.getMessage());
                }
                keysFound++;
            } else {
                record.addToRecord(null);
            }
        }
        if (keysFound != columns.size()) {
            throw new RuntimeException("Invalid data!");
        }
        this.getHelper().getSelectedTable().incrementAffectedRecords();
        this.dbHelper.getSelectedTable().addRecord(record);
    }

    @Override
    public void insertIntoTable(String tableName, String... values) throws RuntimeException {
        this.dbHelper.readTable(tableName);
        Map<String, Class<?>> tableColumns = dbHelper.getSelectedTable().getHeader();
        if (values.length != tableColumns.size()) {
            throw new RuntimeException("Invalid data!");
        }
        int index = 0;
        Record record = new Record(tableColumns);
        for (Class<?> type : tableColumns.values()) {
            try {
                record.addToRecord(TypeFactory.parseToObject(type, values[index++].trim()));
            } catch (ClassCastException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        this.getHelper().getSelectedTable().incrementAffectedRecords();
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
        this.getHelper().resetTables();
        this.dbmsController.getSQLParserController().getSqlParserHelper().getCurrentQuery()
                .execute(this);
        this.dbmsController.getSQLParserController().getSqlParserHelper().getCurrentQuery()
                .getClauses().forEach(clause -> clause.execute(this.dbmsClause));
        if (App.checkForExistence(this.dbHelper.getSelectedTable())) {
            try {
                if (this.dbmsController.getSQLParserController().getSqlParserHelper()
                        .getCurrentQuery() instanceof Writable)
                    this.dbHelper.getBackEndWriter()
                            .writeTable(this.getHelper().getSelectedTable());
            } catch (IOException e) {
                throw new RuntimeException("Error while attempting to write table");
            }

        }
    }

    private void updateTable(SelectionTable selectedTable, Map<Integer, Object> values) {
        for (Record record : selectedTable.getRecordList()) {
            for (Entry<Integer, Object> entry : values.entrySet()) {
                record.getValues().set(entry.getKey(), entry.getValue());
            }
            this.getHelper().getSelectedTable().incrementAffectedRecords();
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
                    newValues.put(index,
                            TypeFactory.parseToObject(entry.getValue(), columns.get(key.trim())));
                } catch (ClassCastException e) {
                    throw new RuntimeException(e.getMessage());
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
            throw new RuntimeException("Database does not exist!");
        }
        this.dbHelper.setDatabase(usedDatabaseDir);
        this.dbHelper.loadTables(dbHelper.getCurrentDatabase());
    }
}
