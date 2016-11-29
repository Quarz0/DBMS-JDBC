package model.statements;

import java.util.ArrayList;
import java.util.List;

import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Insert implements Query {

    private String tableIdentifier;
    private List<String> values;
    private List<String> identifiers;
    private boolean isDefaultSelection;

    public Insert() {
        this.values = new ArrayList<>();
        this.identifiers = new ArrayList<>();
        this.isDefaultSelection = false;
    }

    @Override
    public boolean parse(String s) {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            return false;
        return true;
    }

    private boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_INSERT);
        if (App.checkForExistence(groups)) {
            this.extractTable(groups[1].trim());
            String[] values = this.extractValues(groups[3].trim());
            if (!App.checkForExistence(values))
                return false;
            if (!App.checkForExistence(groups[2])) {
                this.isDefaultSelection = true;
                if (!this.fillColumns(values, null))
                    return false;
            } else {
                this.isDefaultSelection = false;
                String[] identifiers = this.extractIdentifiers(groups[2].trim());
                if (!this.fillColumns(values, identifiers))
                    return false;
            }
            return true;
        }
        return false;
    }

    private boolean fillColumns(String[] values, String[] identifiers) {
        if (App.checkForExistence(identifiers) && values.length != identifiers.length)
            return false;
        for (int i = 0; i < values.length; i++) {
            if (App.checkForExistence(identifiers)) {
                this.values.add(values[i].trim());
                this.identifiers.add(identifiers[i].trim());
            } else {
                this.values.add(values[i].trim());
            }
        }
        return true;
    }

    private String[] extractValues(String s) {
        if (!s.matches(Regex.PARSE_WITH_INSERT_TRIM_MATCH))
            return null;
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

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public boolean isDefaultSelection() {
        return isDefaultSelection;
    }

    @Override
    public void setClause(Clause clause) {
        // TODO Auto-generated method stub

    }

    public List<String> getValues() {
        return values;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

}
