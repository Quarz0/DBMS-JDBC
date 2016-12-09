package model.statements;

import java.text.ParseException;

import controller.DBMS;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Use extends Query {

    private String databaseIdentifier;

    public Use() {
        super();
    }

    private boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_USE);
        if (App.checkForExistence(groups)) {
            this.extractDatabase(groups[1].trim());
            return true;
        }
        return false;
    }

    @Override
    public void execute(DBMS dbms) throws RuntimeException {
        dbms.useDatabase(this.getDatabaseIdentifier());
    }

    private void extractDatabase(String s) {
        this.databaseIdentifier = s.trim();
    }

    public String getDatabaseIdentifier() {
        return this.databaseIdentifier;
    }

    @Override
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            throw new ParseException("Invalid", 0);
    }
}
