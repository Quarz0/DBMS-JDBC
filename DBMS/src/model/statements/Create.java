package model.statements;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import controller.DBMS;
import model.ClassFactory;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Create extends Query {

    private List<String> columns;
    private String databaseIdentifier;
    private boolean isDatabase;
    private String tableIdentifier;
    private List<Class<?>> types;

    public Create() {
        super();
        this.columns = new ArrayList<>();
        this.types = new ArrayList<>();
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
        // TODO Auto-generated method stub

    }

    private boolean extractColIdentifiers(String s) {
        String[] colmuns = s.split(",");
        String[] colmun;
        ClassFactory classFactory = new ClassFactory();
        for (int i = 0; i < colmuns.length; i++) {
            colmun = colmuns[i].trim().split(" ");
            if (App.checkForExistence(classFactory.getClass(colmun[1].trim()))) {
                this.columns.add(colmun[0].trim());
                this.types.add(classFactory.getClass(colmun[1].trim()));
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

    public List<String> getColumns() {
        return columns;
    }

    public String getDatabaseIdentifier() {
        return this.databaseIdentifier;
    }

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public List<Class<?>> getTypes() {
        return types;
    }

    public boolean isDatabase() {
        return this.isDatabase;
    }

    @Override
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            throw new ParseException("Invalid", 0);
    }

}
