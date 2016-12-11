package model.statements;

import java.text.ParseException;

import controller.DBMS;
import model.TypeFactory;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Alter extends Query implements Writable {

    private static final int ADD = 0;
    private static final int DROP = 1;
    private static final int MODIFY = 2;
    private static final int NONE = -1;

    private String column;
    private int state;
    private String tableIdentifier;
    private Class<?> type;

    public Alter() {
        super();
        this.tableIdentifier = column = null;
        this.state = NONE;
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

    @Override
    public void execute(DBMS dbms) throws RuntimeException {
        switch (this.state) {
        case ADD:
            dbms.alterTableAdd(this.getTableIdentifier(), this.getColumn(), this.getType());
            break;
        case DROP:
            dbms.alterTableDrop(this.getTableIdentifier(), this.getColumn());
            break;
        case MODIFY:
            dbms.alterTableModify(this.getTableIdentifier(), this.getColumn(), this.getType());
            break;
        }
    }

    private void extractColumn(String s) {
        this.column = s.trim();
    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
    }

    private void extractType(String s) {
        this.type = TypeFactory.getClass(s.trim().toLowerCase());
    }

    public String getColumn() {
        return this.column;
    }

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public Class<?> getType() {
        return this.type;
    }

    @Override
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            throw new ParseException("Syntax error!(not a valid alter statement)", 0);
    }

}
