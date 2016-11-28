package controller;

import javax.script.ScriptException;

import model.DatabaseHelper;
import model.Observer;
import model.Record;
import model.statements.Query;
import util.App;
import util.BooleanEvaluator;

public class DatabaseController implements DBMS, Observer {
    private DBMSController dbmsController;
    private DatabaseHelper dbHelper;

    public DatabaseController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        dbHelper = new DatabaseHelper(this);
    }

    public DatabaseHelper getHelper() {
        return dbHelper;
    }

    @Override
    public void create(Query query) {

    }

    @Override
    public void drop(Query query) {

    }

    @Override
    public void insertIntoTable(Query query) {

    }

    @Override
    public void updateTable(Query query) {

    }

    @Override
    public void selectFromTable(Query query) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteFromTable(Query query) {

    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    private boolean evaluate(String expression, Record record) throws ScriptException {
        String exp = getFilledExpression(expression, record);
        exp = exp.toLowerCase();
        exp = App.replace(exp, "and", " && ");
        exp = App.replace(exp, "or", " || ");
        exp = App.replace(exp, "not", " ! ");
        return BooleanEvaluator.evaluate(exp);
    }

    private String getFilledExpression(String expression, Record record) {
        String exp = expression;
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

}
