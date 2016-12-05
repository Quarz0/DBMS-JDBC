package model.statements;

import java.text.ParseException;

import controller.DBMS;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Delete extends Query {

    private boolean isAll;
    private String tableIdentifier;
    private Where where;

    public Delete() {
        super();
        this.isAll = false;
    }

    private boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_DELETE_ALL);
        if (App.checkForExistence(groups)) {
            this.extractTable(groups[2].trim());
            return this.isAllFromRegex(groups[1]);
        }
        return false;
    }

    @Override
    public void execute(DBMS dbms) throws RuntimeException {
        // TODO Auto-generated method stub

    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
    }

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public Where getWhere() {
        return this.where;
    }

    public boolean isAll() {
        return this.isAll;
    }

    private boolean isAllFromRegex(String s) {
        if (App.checkForExistence(s) && App.checkForExistence(this.where))
            return false;
        else if (App.checkForExistence(s))
            this.isAll = true;
        else
            this.isAll = false;
        return true;
    }

    @Override
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            throw new ParseException("Invalid", 0);
    }

}
