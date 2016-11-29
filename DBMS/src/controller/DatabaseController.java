package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.script.ScriptException;
import model.ClassFactory;
import model.Database;
import model.DatabaseFilterGenerator;
import model.DatabaseHelper;
import model.ObjectFactory;
import model.Observer;
import model.Record;
import model.SelectionTable;
import model.Table;
import model.statements.Create;
import model.statements.Delete;
import model.statements.Drop;
import model.statements.Insert;
import model.statements.Query;
import model.statements.Select;
import model.statements.Update;
import model.statements.Use;
import util.App;
import util.BooleanEvaluator;
import util.ErrorCode;

public class DatabaseController implements DBMS, Observer {
    private DBMSController dbmsController;
    private DatabaseHelper dbHelper;
    private DatabaseFilterGenerator databaseFilter;
    private ClassFactory classFactory;
    private ObjectFactory objectFactory;

    public DatabaseController(DBMSController dbmsController) {
        databaseFilter = new DatabaseFilterGenerator();
        classFactory = new ClassFactory();
        objectFactory = new ObjectFactory();
        this.dbmsController = dbmsController;
        this.dbHelper = new DatabaseHelper(this);
    }

    public DatabaseHelper getHelper() {
        return dbHelper;
    }

    @Override
    public void update() {
        Query currentQuery = this.dbmsController.getSQLParserController().getSqlParserHelper()
                .getCurrentQuery();
        if (currentQuery instanceof Create) {
            this.handleCreate((Create) currentQuery);
        } else if (currentQuery instanceof Delete) {
            this.handleDelete((Delete) currentQuery);
        } else if (currentQuery instanceof Drop) {
            this.handleDrop((Drop) currentQuery);
        } else if (currentQuery instanceof Insert) {
            this.handleInsert((Insert) currentQuery);
        } else if (currentQuery instanceof Select) {
            this.handleSelect((Select) currentQuery);
        } else if (currentQuery instanceof Update) {
            this.handleUpdate((Update) currentQuery);
        } else if (currentQuery instanceof Use) {
            this.handleUse((Use) currentQuery);
        } else {
            this.dbmsController.getSQLParserController().callForFailure(ErrorCode.SYNTAX_ERROR);
        }
    }

    private void handleCreate(Create create) {
        if (create.isDatabase()) {
            this.dbmsController.getCLIController()
                    .status(this.createDatabase(create.getDatabaseIdentifier()));
        } else {
            this.dbmsController.getCLIController().status(this.createTable(
                    create.getTableIdentifier(), create.getColumns(), create.getTypes()));
        }
    }

    private void handleDelete(Delete delete) {
        this.dbmsController.getCLIController().status(this
                .deleteFromTable(delete.getTableIdentifier(), delete.getWhere().getExpression()));
    }

    private void handleDrop(Drop drop) {
        if (drop.isDatabase())
            this.dbmsController.getCLIController()
                    .status(this.dropDatabase(drop.getDatabaseIdentifier()));
        else
            this.dbmsController.getCLIController()
                    .status(this.dropTable(drop.getTableIdentifier()));
    }

    private void handleInsert(Insert insert) {
        if (insert.isDefaultSelection()) {
            this.dbmsController.getCLIController()
                    .status(this.insertIntoTable(insert.getTableIdentifier(), insert.getValues()));
        } else {
            this.dbmsController.getCLIController().status(this.insertIntoTable(
                    insert.getTableIdentifier(), insert.getIdentifiers(), insert.getValues()));
        }
    }

    private void handleSelect(Select select) {
        this.dbmsController.getCLIController()
                .status(this.selectFromTable(select.getTableIdentifier(), select.getColumns(),
                        select.getWhere().getExpression()));
    }

    private void handleUpdate(Update update) {
        this.dbmsController.getCLIController().status(this.updateTable(update.getTableIdentifier(),
                update.getColumns(), update.getValues(), update.getWhere().getExpression()));
    }

    private void handleUse(Use use) {
        this.dbmsController.getCLIController()
                .status(this.useDatabase(use.getDatabaseIdentifier()));
    }

    @Override
    public boolean useDatabase(String databaseName) {
        File usedDatabaseDir = getDatabase(databaseName);
        if (!App.checkForExistence(usedDatabaseDir)) {
            this.dbmsController.getCLIController().callForFailure(ErrorCode.DATABASE_NOT_FOUND);
            return false;
        }
        this.dbHelper.setDatabase(usedDatabaseDir);
        this.reLoadTables(dbHelper.getCurrentDatabase());
        return true;
    }

    @Override
    public boolean createDatabase(String databaseName) {
        if (databaseExists(databaseName)) {
            this.dbmsController.getCLIController().callForFailure(ErrorCode.DATABASE_DUPLICATION);
            return false;
        }
        new Database(this.dbHelper.getWorkspaceDir().getAbsolutePath(), databaseName);
        return true;
    }

    @Override
    public boolean createTable(String tableName, List<String> colNames, List<Class<?>> types) {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            this.dbmsController.getCLIController()
                    .callForFailure(ErrorCode.DATABASE_IS_NOT_SELECTED);
            return false;
        }
        if (colNames.size() != types.size() || containsDublicates(colNames)) {
            throw new RuntimeException("Wrong data entered");
        }
        if (this.tableExists(tableName)) {
            throw new RuntimeException("Table already exists");
        }
        File tableFile = new File(
                dbHelper.getCurrentDatabase().getPath() + File.separator + tableName);
        Table table = new Table(tableFile);
        dbHelper.getCurrentDatabase().registerTable(table);
        File xmlFile = dbmsController.getXMLController().initializeXML(table.getTablePath(),
                tableName, colNames, types);
        File dtdFile = dbmsController.getXMLController().initializeDTD(table.getTablePath(),
                tableName, colNames, types);
        table.registerFiles(xmlFile, dtdFile);
        return true;
    }

    @Override
    public boolean dropTable(String tableName) {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("No database selected.");
            // return false;
        }
        int index = 0;
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (equalStrings(table.getTableName(), tableName)) {
                deleteDir(table.getTableDir());
                dbHelper.getCurrentDatabase().getTables().remove(index);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dropDatabase(String databaseName) {
        File database = getDatabase(databaseName);
        if (!App.checkForExistence(database)) {
            return false;
        }
        deleteDir(database);
        return true;
    }

    @Override
    public boolean insertIntoTable(String tableName, List<String> colNames,
            List<String> valuesAsString) {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("No database selected.");
            // return false;
        }
        if (containsDublicates(colNames) || colNames.size() != valuesAsString.size()) {
            return false;
        }
        Table table = getTable(tableName);
        if (!App.checkForExistence(table)) {
            throw new RuntimeException("Cannot find table");
        }
        List<String> typesAsStrings = dbmsController.getXMLController().getTypes(table);
        List<String> tableColNames = dbmsController.getXMLController().getColumnsNames(table);
        List<Class<?>> types = getTypesFromStrings(typesAsStrings);
        List<Object> values = getObjectsFromStrings(types, valuesAsString);
        for (String colName : colNames) {
            if (!tableColNames.contains(colName)) {
                throw new RuntimeException("Data doesnot match table.");
            }
        }
        if (!isMatched(tableColNames, colNames, types, values)) {
            throw new RuntimeException("Data doesnot match table.");
        }
        List<Object> generateValues = new ArrayList<>();
        for (int i = 0; i < tableColNames.size(); i++) {
            boolean found = false;
            for (int j = 0; j < colNames.size(); j++) {
                if (equalStrings(tableColNames.get(i), colNames.get(j))) {
                    generateValues.add(values.get(j));
                    found = true;
                    break;
                }
            }
            if (!found) {
                generateValues.add(null);
            }
        }
        Record record = new Record(generateValues);
        return dbmsController.getXMLController().insertIntoTable(table, record);
    }

    @Override
    public boolean insertIntoTable(String tableName, List<String> valuesAsString) {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("No database selected.");
            // return false;
        }
        Table table = getTable(tableName);
        if (!App.checkForExistence(table)) {
            throw new RuntimeException("Cannot find table");
        }
        List<String> typesAsStrings = dbmsController.getXMLController().getTypes(table);
        List<Class<?>> types = getTypesFromStrings(typesAsStrings);
        List<Object> values = getObjectsFromStrings(types, valuesAsString);
        if (values.size() != types.size()) {
            throw new RuntimeException("Wrong data");
        }
        for (int i = 0; i < types.size(); i++) {
            if (!types.get(i).isInstance(values.get(i))) {
                throw new RuntimeException("Wrong data");
            }
        }
        Record record = new Record(values);
        return dbmsController.getXMLController().insertIntoTable(table, record);
    }

    @Override
    public boolean selectFromTable(String tableName, List<String> colNames, String condition) {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("No database selected.");
            // return false;
        }
        Table table = getTable(tableName);
        if (!App.checkForExistence(table)) {
            throw new RuntimeException("Cannot find table");
        }
        if (containsDublicates(colNames)) {
            throw new RuntimeException("Data contains dublicates");
        }
        List<String> tableColNames = dbmsController.getXMLController().getColumnsNames(table);
        if (!containsAllStrings(tableColNames, colNames)) {
            throw new RuntimeException("Wrong column names");
            // return false;
        }
        try {
            SelectionTable selectedTable = dbmsController.getXMLController().selectFromTable(table,
                    colNames, condition);
            selectedTable = reformTable(selectedTable, colNames);
            dbHelper.setSelectedTable(selectedTable);
        } catch (Exception e) {
            throw new RuntimeException("Cannot read Table");
            // return false;
        }
        return true;
    }

    @Override
    public boolean updateTable(String tableName, List<String> colNames, List<String> valuesAsString,
            String condition) {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("No database selected.");
            // return false;
        }
        Table table = getTable(tableName);
        if (!App.checkForExistence(table)) {
            throw new RuntimeException("Cannot find table");
        }
        List<String> tableColNames = dbmsController.getXMLController().getColumnsNames(table);
        List<String> typesAsStrings = dbmsController.getXMLController().getTypes(table);
        List<Class<?>> types = getTypesFromStrings(typesAsStrings);
        List<Object> values = getObjectsFromStrings(types, valuesAsString);
        if (colNames.size() != values.size() || containsDublicates(colNames)
                || !isMatched(tableColNames, colNames, types, values)) {
            throw new RuntimeException("Wrong data inserted");
            // return false;
        }
        try {
            dbmsController.getXMLController().updateTable(table, colNames, values, condition);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong Cannot update table.");
            // return false;
        }
        return true;
    }

    @Override
    public boolean deleteFromTable(String tableName, String condition) {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("No database selected.");
            // return false;
        }
        Table table = getTable(tableName);
        if (!App.checkForExistence(table)) {
            throw new RuntimeException("Cannot find table");
        }
        try {
            dbmsController.getXMLController().removeFromTable(table, condition);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong Cannot remove from table.");
            // return false;
        }
        return true;
    }

    public boolean evaluate(String expression, Record record) throws ScriptException {
        String exp = getFilledExpression(expression, record);
        exp = exp.toLowerCase();
        exp = App.replace(exp, "and", " && ");
        exp = App.replace(exp, "or", " || ");
        exp = App.replace(exp, "not", " ! ");
        return BooleanEvaluator.evaluate(exp);
    }

    private Table getTable(String tableName) {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("No database selected.");
            // return false;
        }
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (equalStrings(table.getTableName(), tableName)) {
                return table;
            }
        }
        return null;
    }

    private List<Class<?>> getTypesFromStrings(List<String> typesAsStrings) {
        List<Class<?>> types = new ArrayList<>();
        for (int i = 0; i < typesAsStrings.size(); i++) {
            types.add(classFactory.getClass(typesAsStrings.get(i)));
        }
        return types;
    }

    private <T> boolean containsDublicates(List<T> list) {
        Set<T> temp = new HashSet<>(list);
        if (temp.size() != list.size()) {
            return true;
        }
        return false;
    }

    private boolean databaseExists(String databaseName) {
        File database = getDatabase(databaseName);
        if (!App.checkForExistence(database)) {
            return false;
        }
        return true;
    }

    private boolean tableExists(String tableName) {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            throw new RuntimeException("No database selected.");
            // return false;
        }
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (equalStrings(table.getTableName(), tableName)) {
                return true;
            }
        }
        return false;
    }

    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (App.checkForExistence(contents)) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    private File getDatabase(String databaseName) {
        File[] databases = dbHelper.getDatabaseDir().listFiles(databaseFilter);
        for (File databaseFile : databases) {
            if (equalStrings(databaseFile.getName(), databaseName)) {
                return databaseFile;
            }
        }
        return null;
    }

    private void reLoadTables(Database database) {
        database.clearTableList();
        File[] tables = database.getDatabaseDir().listFiles(databaseFilter);
        for (File tableDir : tables) {
            Table table = new Table(tableDir);
            File xmlFile = new File(
                    table.getTablePath() + File.separator + table.getTableName() + ".xml");
            File dtdFile = new File(
                    table.getTablePath() + File.separator + table.getTableName() + ".dtd");
            if (!(xmlFile.exists() && dtdFile.exists())) {
                throw new RuntimeException("Missing table files");
            }
            table.registerFiles(xmlFile, dtdFile);
            database.getTables().add(table);
        }
    }

    private String getFilledExpression(String expression, Record record) {
        String exp = expression.toLowerCase();
        for (int i = 0; i < record.getColumns().size(); i++) {
            if (record.getValues().get(i) instanceof String) {
                exp = App.replace(exp, record.getColumns().get(i).toLowerCase(),
                        "\"" + record.getValues().get(i).toString() + "\"");
            } else {
                exp = App.replace(exp, record.getColumns().get(i).toLowerCase(),
                        record.getValues().get(i).toString());
            }
        }
        return exp;
    }

    private boolean isMatched(List<String> tableColNames, List<String> colNames,
            List<Class<?>> types, List<Object> values) {
        for (int i = 0; i < colNames.size(); i++) {
            for (int j = 0; j < tableColNames.size(); j++) {
                if (equalStrings(colNames.get(i), tableColNames.get(j))) {
                    if (!types.get(j).isInstance(values.get(i))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean equalStrings(String str1, String str2) {
        return str1.toLowerCase().equals(str2.toLowerCase());
    }

    private SelectionTable reformTable(SelectionTable selectedTable, List<String> colNames) {
        SelectionTable ret = new SelectionTable(colNames);
        List<Object> values;
        for (int i = 0; i < selectedTable.getRecordList().size(); i++) {
            values = new ArrayList<>();
            for (String str : colNames) {
                for (int j = 0; j < selectedTable.getHeader().size(); j++) {
                    if (equalStrings(str, selectedTable.getHeader().get(j))) {
                        values.add(selectedTable.getRecordList().get(i).getValues().get(j));
                    }
                }
            }
            ret.addRecord(new Record(values));
        }
        return ret;
    }

    private boolean containsAllStrings(List<String> list1, List<String> list2) {
        for (String str2 : list2) {
            boolean found = false;
            for (String str1 : list1) {
                if (equalStrings(str1, str2)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private List<Object> getObjectsFromStrings(List<Class<?>> types, List<String> values) {
        List<Object> ret = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            ret.add(objectFactory.parseToObject(types.get(i), values.get(i)));
        }
        return ret;
    }
}
