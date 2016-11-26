package model.statements;

import java.util.ArrayList;
import java.util.List;

import model.Database;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Create implements Query {

    private String databaseIdentifier;
    private String tableIdentifier;
    private List<String> colIdentifiers;
    private List<Class<?>> colTypes;
    private boolean isDatabase;

    public Create() {
        this.colIdentifiers = new ArrayList<>();
        this.colTypes = new ArrayList<>();
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

        return false;
    }

    private void extractDatabase(String s) {

    }

    private void callForFailure(/* Exception e */) {

    }

}
