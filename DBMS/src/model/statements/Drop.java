package model.statements;

import java.text.ParseException;

import controller.DBMS;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Drop extends Query implements Writable {

    private String databaseIdentifier;
    private boolean isDatabase;
    private String tableIdentifier;

    public Drop() {
        super();
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

    @Override
    public void execute(DBMS dbms) throws RuntimeException {
        if (this.isDatabase()) {
            dbms.dropDatabase(this.getDatabaseIdentifier());
        } else {
            dbms.dropTable(this.getTableIdentifier());
        }
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
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            throw new ParseException("Syntax error!(not a valid drop statement)", 0);
    }

}
