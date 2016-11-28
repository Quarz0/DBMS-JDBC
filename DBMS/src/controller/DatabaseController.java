package controller;

import javax.script.ScriptException;

import model.DatabaseHelper;
import model.Observer;
import model.Record;
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

public class DatabaseController implements DBMS, Observer {
    private DBMSController dbmsController;
    private DatabaseHelper dbHelper;

    public DatabaseController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        this.dbHelper = new DatabaseHelper(this);
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

        }
    }

    private void handleCreate(Create create) {

    }

    private void handleDelete(Delete delete) {

    }

    private void handleDrop(Drop drop) {

    }

    private void handleInsert(Insert insert) {

    }

    private void handleSelect(Select select) {

    }

    private void handleUpdate(Update update) {

    }

    private void handleUse(Use use) {

    }

}
