package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import model.statements.Query;
import util.App;
import util.BooleanEvaluator;
import util.ErrorCode;

public class DatabaseController implements DBMS, Observer {
    private ClassFactory classFactory;
    private DatabaseFilterGenerator databaseFilter;
    private DatabaseHelper dbHelper;
    private DBMSController dbmsController;
    private ObjectFactory objectFactory;
    private DBMSClause dbmsClause;

    public DatabaseController(DBMSController dbmsController) {
        databaseFilter = new DatabaseFilterGenerator();
        classFactory = new ClassFactory();
        objectFactory = new ObjectFactory();
        this.dbmsController = dbmsController;
        this.dbHelper = new DatabaseHelper(this);
        this.dbmsClause = new ClauseController(dbmsController);

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

    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (App.checkForExistence(contents)) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    private boolean equalStrings(String str1, String str2) {
        return str1.toLowerCase().equals(str2.toLowerCase());
    }

    public boolean evaluate(String expression, Record record) throws ScriptException {
        String exp = getFilledExpression(expression, record);
        exp = exp.toLowerCase();
        exp = App.replace(exp, "and", " && ");
        exp = App.replace(exp, "or", " || ");
        exp = App.replace(exp, "not", " ! ");
        return BooleanEvaluator.evaluate(exp);
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

    private String getFilledExpression(String expression, Record record) {
        String exp = expression.toLowerCase();
        for (int i = 0; i < record.getColumns().size(); i++) {
            if (exp.charAt(0) == '\"') {
                exp = App.replace(exp, record.getColumns().get(i).toLowerCase(),
                        "\"" + record.getValues().get(i).toString() + "\"");
            } else {
                exp = App.replace(exp, record.getColumns().get(i).toLowerCase(),
                        record.getValues().get(i).toString());
            }
        }
        return exp;
    }

    public DatabaseHelper getHelper() {
        return dbHelper;
    }

    private List<Object> getObjectsFromStrings(List<Class<?>> types, List<String> values) {
        List<Object> ret = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            ret.add(objectFactory.parseToObject(types.get(i), values.get(i)));
        }
        return ret;
    }

    private Table getTable(String tableName) {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            this.dbmsController.getCLIController().callForFailure(ErrorCode.ERROR);
            return null;
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

    private SelectionTable reformTable(SelectionTable selectedTable, List<String> colNames) {
        SelectionTable ret = new SelectionTable(selectedTable.getTableName(), colNames);
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
                this.dbmsController.getCLIController().callForFailure(ErrorCode.ERROR);
            }
            table.registerFiles(xmlFile, dtdFile);
            database.getTables().add(table);
        }
    }

    private boolean tableExists(String tableName) {
        if (!App.checkForExistence(dbHelper.getCurrentDatabase())) {
            this.dbmsController.getCLIController().callForFailure(ErrorCode.ERROR);
            return false;
        }
        for (Table table : dbHelper.getCurrentDatabase().getTables()) {
            if (equalStrings(table.getTableName(), tableName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update() {
        Query currentQuery = this.dbmsController.getSQLParserController().getSqlParserHelper()
                .getCurrentQuery();
    }

    @Override
    public void createDatabase(String databaseName) throws RuntimeException {
        // TODO Auto-generated method stub

    }

    @Override
    public void createTable(String tableName, Map<String, Class<?>> columns)
            throws RuntimeException {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteFromTable(String tableName) throws RuntimeException {
        // TODO Auto-generated method stub

    }

    @Override
    public void dropDatabase(String databaseName) throws RuntimeException {
        // TODO Auto-generated method stub

    }

    @Override
    public void dropTable(String tableName) throws RuntimeException {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }

}
