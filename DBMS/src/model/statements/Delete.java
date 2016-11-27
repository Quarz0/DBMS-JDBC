package model.statements;

import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Delete implements Query {

    private String tableIdentifier;
    private boolean isAll;
    private Where where;

    public Delete(Where where) {
        this.where = where;
        if (!App.checkForExistence(this.where))
            this.isAll = true;
        else
            this.isAll = false;
    }

    @Override
    public void parse(String s) {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            this.callForFailure();
    }

    private boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_DELETE_ALL);
        if (App.checkForExistence(groups)) {
            this.extractTable(groups[1].trim());
            return true;
        }
        return false;
    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
    }

    private void callForFailure(/* Exception e */) {

    }

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public boolean isAll() {
        return this.isAll;
    }

    public Where getWhere() {
        return this.where;
    }

}
