package model.statements;

import java.util.ArrayList;
import java.util.List;

import model.Pair;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Insert implements Query {

    private String tableIdentifier;
    private List<Pair<String, String>> columns;
    private boolean isDefaultSelection;

    public Insert() {
        this.columns = new ArrayList<>();
        this.isDefaultSelection = false;
    }

    @Override
    public void parse(String s) {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            this.callForFailure();
    }

    private boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_INSERT);
        if (App.checkForExistence(groups)) {
            this.extractTable(groups[1].trim());
            String[] values = this.extractValues(groups[3].trim());
            if (!App.checkForExistence(groups[2])) {
                this.isDefaultSelection = true;
                this.fillColumns(values, null);
            } else {
                this.isDefaultSelection = false;
                String[] identifiers = this.extractIdentifiers(groups[2].trim());
                this.fillColumns(values, identifiers);
            }
            return true;
        }
        return false;
    }

    private void fillColumns(String[] values, String[] identifiers) {
        Pair<String, String> pair;
        if (App.checkForExistence(identifiers) && values.length != identifiers.length)
            this.callForFailure();
        for (int i = 0; i < values.length; i++) {
            if (App.checkForExistence(identifiers))
                pair = new Pair<String, String>(values[i], identifiers[i]);
            else
                pair = new Pair<String, String>(values[i], null);
            this.columns.add(pair);
        }
    }

    private String[] extractValues(String s) {
        if (!s.matches(Regex.PARSE_WITH_INSERT_TRIM_MATCH))
            this.callForFailure();
        if (s.split(Regex.PARSE_WITH_INSERT_SPLIT_PATTERN1).length <= s
                .split(Regex.PARSE_WITH_INSERT_SPLIT_PATTERN2).length)
            return s.split(Regex.PARSE_WITH_INSERT_SPLIT_PATTERN1);
        else
            return s.split(Regex.PARSE_WITH_INSERT_SPLIT_PATTERN2);
    }

    private String[] extractIdentifiers(String s) {
        return s.split(",");
    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
    }

    private void callForFailure(/* Exception e */) {

    }

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public List<Pair<String, String>> getColumns() {
        return columns;
    }

    public boolean isDefaultSelection() {
        return isDefaultSelection;
    }

    @Override
    public void setClause(Clause clause) {
        // TODO Auto-generated method stub
        
    }

}
