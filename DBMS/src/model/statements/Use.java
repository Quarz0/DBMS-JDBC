package model.statements;

import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Use implements Query {

    private String databaseIdentifier;

    public Use() {
    }

    private boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_USE);
        if (App.checkForExistence(groups)) {
            this.extractDatabase(groups[1].trim());
            return true;
        }
        return false;
    }

    private void extractDatabase(String s) {
        this.databaseIdentifier = s.trim();
    }

    public String getDatabaseIdentifier() {
        return this.databaseIdentifier;
    }

    @Override
    public boolean parse(String s) {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            return false;
        return true;
    }

    @Override
    public void setClause(Clause clause) {
        // TODO Auto-generated method stub

    }

}
