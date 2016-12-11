package model.statements;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import controller.DBMS;
import controller.backEnd.JSONWriter;
import controller.backEnd.XMLWriter;
import model.ClassFactory;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Create extends Query implements Writable {

    private Map<String, Class<?>> columns;
    private String databaseIdentifier;
    private boolean isDatabase;
    private String tableIdentifier;

    public Create() {
        super();
        this.columns = new LinkedHashMap<>();
        this.isDatabase = false;
    }

    private boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_CREATE_DATABASE);
        if (App.checkForExistence(groups)) {
            this.extractDatabase(groups[1].trim());
            return true;
        }
        groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_CREATE_TABLE);
        if (App.checkForExistence(groups)) {
            this.extractTable(groups[1].trim());
            return this.extractColIdentifiers(groups[2].trim());
        }
        return false;
    }

    @Override
    public void execute(DBMS dbms) throws RuntimeException {
        if (this.isDatabase()) {
            dbms.createDatabase(this.getDatabaseIdentifier());
        } else {
            dbms.createTable(this.getTableIdentifier(), this.getColumns(), new XMLWriter());
        }
    }

    private boolean extractColIdentifiers(String s) {
        String[] colmuns = s.split(",");
        String[] column;

        for (int i = 0; i < colmuns.length; i++) {
            column = colmuns[i].trim().split(" ");
            if (App.checkForExistence(ClassFactory.getClass(column[1].trim()))) {
                this.columns.put(column[0].trim(), ClassFactory.getClass(column[1].trim()));
            } else {
                return false;
            }
        }
        return true;
    }

    private void extractDatabase(String s) {
        this.databaseIdentifier = s.trim();
        this.isDatabase = true;
    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
        this.isDatabase = false;
    }

    public Map<String, Class<?>> getColumns() {
        return columns;
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
            throw new ParseException("Syntax error!(not a valid create statement)", 0);
    }

}
