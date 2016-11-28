package model.statements;

import java.util.ArrayList;
import java.util.List;

import model.ClassFactroy;
import model.Pair;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Create implements Query {

    private String databaseIdentifier;
    private String tableIdentifier;
    private List<Pair<String, Class<?>>> columns;
    private boolean isDatabase;

    public Create() {
        this.columns = new ArrayList<>();
        this.isDatabase = false;
    }

    @Override
    public void parse(String s) {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            this.callForFailure();
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
            this.extractColIdentifiers(groups[2].trim());
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

    private void extractColIdentifiers(String s) {
        String[] colmuns = s.split(",");
        String[] colmun;
        ClassFactroy classFactroy = new ClassFactroy();
        for (int i = 0; i < colmuns.length; i++) {
            colmun = colmuns[i].trim().split(" ");
            if (App.checkForExistence(classFactroy.getClass(colmun[1].trim()))) {
                Pair<String, Class<?>> pair = new Pair<String, Class<?>>(colmun[0].trim(),
                        classFactroy.getClass(colmun[1].trim()));
                this.columns.add(pair);
            } else {
                this.callForFailure();
            }
        }
    }

    private void callForFailure(/* Exception e */) {

    }

    public String getDatabaseIdentifier() {
        return this.databaseIdentifier;
    }

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public List<Pair<String, Class<?>>> getColumns() {
        return this.columns;
    }

    public boolean isDatabase() {
        return this.isDatabase;
    }

    @Override
    public void setClause(Clause clause) {

    }

}
