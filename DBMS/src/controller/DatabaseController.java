package controller;

import java.util.Map;

import javax.script.ScriptException;

import model.ClassFactory;
import model.DatabaseFilterGenerator;
import model.DatabaseHelper;
import model.ObjectFactory;
import model.Observer;
import model.Record;
import util.App;
import util.BooleanEvaluator;

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

    public boolean evaluate(String expression, Record record) throws ScriptException {
        String exp = getFilledExpression(expression, record);
        exp = exp.toLowerCase();
        exp = App.replace(exp, "and", " && ");
        exp = App.replace(exp, "or", " || ");
        exp = App.replace(exp, "not", " ! ");
        return BooleanEvaluator.evaluate(exp);
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

    @Override
    public void update() {
        this.dbmsController.getSQLParserController().getSqlParserHelper().getCurrentQuery()
                .execute(this);
        this.dbmsController.getSQLParserController().getSqlParserHelper().getCurrentQuery()
                .getClauses().forEach(clause -> clause.execute(this.dbmsClause));
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
