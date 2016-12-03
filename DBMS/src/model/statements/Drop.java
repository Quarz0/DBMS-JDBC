package model.statements;

import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Drop implements Query {

    private String databaseIdentifier;
    private boolean isDatabase;
    private String tableIdentifier;

    public Drop() {
        this.isDatabase = false;
    }

    private boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_DROP_DATABASE);
        if (App.checkForExistence(groups)) {
            this.extractDatabase(groups[1].trim());
            return true;
        }
        groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_DROP_TABLE);
        if (App.checkForExistence(groups)) {
            this.extractTable(groups[1].trim());
            return true;
        }
        return false;
    }

    private void extractDatabase(String s) {
        this.databaseIdentifier = s.trim();
        this.isDatabase = true;
    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
        this.isDatabase = false;
    }

    public String getDatabaseIdentifier() {
        return this.databaseIdentifier;
    }

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public boolean isDatabase() {
        return this.isDatabase;
    }

    @Override
    public boolean parse(String s) {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            return false;
        return true;
    }

    @Override
    public void setClause(Clause clause) {

    }

}
