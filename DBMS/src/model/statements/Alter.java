package model.statements;

import java.text.ParseException;

import controller.DBMS;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Alter extends Query {

    private static final int NONE = -1;
    private static final int ADD = 0;
    private static final int DROP = 1;
    private static final int MODIFY = 2;

    private int state;
    private String tableIdentifier;
    private String type;
    private String column;

    public Alter() {
        tableIdentifier = column = type = null;
        state = NONE;
    }

    private boolean checkRegex(String s) {

        String[] groups;
        if (App.checkForExistence(
                groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_ALTER_TABLE_ADD))) {
            state = ADD;
            extractTable(groups[1]);
            extractColumn(groups[2]);
            extractType(groups[3]);
            return true;
        } else if (App.checkForExistence(
                groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_ALTER_TABLE_DROP_COLUMN))) {
            state = DROP;
            extractTable(groups[1]);
            extractColumn(groups[2]);
            return true;
        } else if (App.checkForExistence(
                groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_ALTER_TABLE_MODIFY_COLUMN))) {
            state = MODIFY;
            extractTable(groups[1]);
            extractColumn(groups[2]);
            extractType(groups[3]);
            return true;
        }
        return false;
    }

    private void extractColumn(String s) {
        this.column = s.trim();
    }

    private void extractType(String s) {
        this.type = s.trim();
    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
    }

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public String getColumn() {
        return this.column;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public void parse(String s) throws ParseException {
        //if (!App.checkForExistence(s) || !this.checkRegex(s))
            // TODO
    }

    @Override
    public void execute(DBMS dbms) throws RuntimeException {
        // TODO Auto-generated method stub

    }

}
