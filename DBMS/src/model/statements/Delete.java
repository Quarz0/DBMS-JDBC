package model.statements;

import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Delete implements Query {

    private boolean isAll;
    private String tableIdentifier;
    private Where where;

    public Delete() {
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
    public boolean parse(String s) {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            return false;
        return true;
    }

    @Override
    public void setClause(Clause clause) {
        if (clause instanceof Where)
            this.where = (Where) clause;
    }

}
