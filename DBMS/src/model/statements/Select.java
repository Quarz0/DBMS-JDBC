package model.statements;

import java.text.ParseException;

import controller.DBMS;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Select extends Query implements Viewable {

    private String[] columns;
    private boolean isAll;
    private String tableIdentifier;

    public Select() {
        super();
        this.isAll = false;
    }

    public boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_SELECT_ALL_FROM);
        if (App.checkForExistence(groups)) {
            this.extractTable(groups[1].trim());
            this.isAll = true;
            return true;
        } else {
            groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_SELECT_FROM);
            if (App.checkForExistence(groups)) {
                this.extractTable(groups[2].trim());
                return this.extractColIdentifiers(groups[1].trim());
            }
        }
        return false;
    }

    @Override
    public void execute(DBMS dbms) throws RuntimeException {
        if (this.isAll()) {
            dbms.selectAllFromTable(this.getTableIdentifier());
        } else {
            dbms.selectFromTable(this.getTableIdentifier(), columns);
        }
    }

    private boolean extractColIdentifiers(String s) {
        this.columns = s.split(",");
        for (int i = 0; i < this.columns.length; i++) {
            this.columns[i] = this.columns[i].trim();
            if (!App.isLegalIdentifier(this.columns[i])) {
                return false;
            }
        }
        return true;
    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
    }

    public String[] getColumns() {
        return this.columns;
    }

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public boolean isAll() {
        return isAll;
    }

    @Override
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            throw new ParseException("Syntax error!(not a valid select statement)", 0);
    }
}
