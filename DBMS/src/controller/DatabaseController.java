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
import model.Observer;
import model.Record;
import model.SelectionTable;
import model.Table;
import util.App;
import util.BooleanEvaluator;

public class DatabaseController implements DBMS, Observer {
    private DBMSController dbmsController;
    private DatabaseHelper dbHelper;
    private DatabaseFilterGenerator databaseFilter;
    private ClassFactory classFactory;

    public DatabaseController(DBMSController dbmsController) {
        databaseFilter = new DatabaseFilterGenerator();
        classFactory = new ClassFactory();
        this.dbmsController = dbmsController;
        dbHelper = new DatabaseHelper(this);
    }

    public DatabaseHelper getHelper() {
        return dbHelper;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean useDatabase(String databaseName) {
        File usedDatabase = getDatabase(databaseName);
        if (usedDatabase == null) {
            throw new RuntimeException("Database doesnot exist");
            // return false;
        }
        dbHelper.getCurrentDatabase().useDatabase(usedDatabase);
        reLoadTables(dbHelper.getCurrentDatabase());
        return true;
    }

    @Override
    public boolean createDatabase(String databaseName) {
        if (databaseExists(databaseName)) {
            throw new RuntimeException("Database already exists");
            // return false;
        }
        new Database(dbHelper.getWorkSpacePath(), databaseName);
        return true;
    }

    @Override
    public boolean createTable(String tableName, List<String> colNames, List<Class<?>> types) {
        if (colNames.size() != types.size() || containsDublicates(colNames)) {
            throw new RuntimeException("Wrong data entered");
        }
        if (tableExists(tableName)) {
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
        if (database == null) {
            return false;
        }
        deleteDir(database);
        return true;
    }

    @Override
    public boolean insertIntoTable(String tableName, List<String> colNames, List<Object> values) {
        if (containsDublicates(colNames) || colNames.size() != values.size()) {
            return false;
        }
        Table table = getTable(tableName);
        if (table == null) {
            throw new RuntimeException("Cannot find table");
        }
        List<String> typesAsStrings = dbmsController.getXMLController().getTypes(table);
        List<String> tableColNames = dbmsController.getXMLController().getColumnsNames(table);
        List<Class<?>> types = getTypesFromStrings(typesAsStrings);
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
    public boolean insertIntoTable(String tableName, List<Object> values) {
        Table table = getTable(tableName);
        if (table == null) {
            throw new RuntimeException("Cannot find table");
        }
        List<String> typesAsStrings = dbmsController.getXMLController().getTypes(table);
        List<Class<?>> types = getTypesFromStrings(typesAsStrings);
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
        Table table = getTable(tableName);
        if (table == null) {
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
    public boolean updateTable(String tableName, List<String> colNames, List<Object> values,
            String condition) {
        Table table = getTable(tableName);
        if (table == null) {
            throw new RuntimeException("Cannot find table");
        }
        List<String> tableColNames = dbmsController.getXMLController().getColumnsNames(table);
        List<String> typesAsStrings = dbmsController.getXMLController().getTypes(table);
        List<Class<?>> types = getTypesFromStrings(typesAsStrings);
        if (colNames.size() != values.size() || containsDublicates(colNames)
                || !isMatched(tableColNames, colNames, types, values)) {
            throw new RuntimeException("Wrong data inserted");
            // return false;
        }
        // update table.
        return true;
    }

    @Override
    public boolean deleteFromTable(String tableName, List<String> colNames, String condition) {
        return false;
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
        if (database == null) {
            return false;
        }
        return true;
    }

    private boolean tableExists(String tableName) {
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (equalStrings(table.getTableName(), tableName)) {
                return true;
            }
        }
        return false;
    }

    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
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
}
