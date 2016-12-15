package eg.edu.alexu.csd.oop.DBMS.model.statements.queries;

import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMS;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Query;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Writable;
import eg.edu.alexu.csd.oop.DBMS.util.App;
import eg.edu.alexu.csd.oop.DBMS.util.Regex;
import eg.edu.alexu.csd.oop.DBMS.util.RegexEvaluator;

public class Insert extends Query implements Writable {

    private Map<String, String> columns;
    private boolean isDefaultSelection;
    private String tableIdentifier;
    String[] values;

    public Insert() {
        super();
        this.isDefaultSelection = false;
        this.columns = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    private boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_INSERT);
        if (App.checkForExistence(groups)) {
            this.extractTable(groups[1].trim());
            this.values = this.extractValues(groups[3].trim());
            if (!App.checkForExistence(values))
                return false;
            if (!App.checkForExistence(groups[2])) {
                this.isDefaultSelection = true;
            } else {
                this.isDefaultSelection = false;
                String[] identifiers = this.extractIdentifiers(groups[2].trim());
                if (!this.fillColumns(this.values, identifiers))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void execute(DBMS dbms) throws RuntimeException {
        if (this.isDefaultSelection()) {
            dbms.insertIntoTable(this.getTableIdentifier(), values);
        } else {
            dbms.insertIntoTable(this.getTableIdentifier(), columns);
        }
    }

    private String[] extractIdentifiers(String s) {
        return s.split(",");
    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
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

    private boolean fillColumns(String[] values, String[] identifiers) {
        if (App.checkForExistence(identifiers) && values.length != identifiers.length)
            return false;
        for (int i = 0; i < values.length; i++) {
            this.columns.put(identifiers[i].trim(), values[i].trim());
        }
        return true;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public String[] getValues() {
        return values;
    }

    public boolean isDefaultSelection() {
        return isDefaultSelection;
    }

    @Override
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            throw new ParseException("Syntax error!(not a valid insert statement)", 0);
    }

}
