package eg.edu.alexu.csd.oop.DBMS.model.statements;

import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMS;
import eg.edu.alexu.csd.oop.DBMS.util.App;
import eg.edu.alexu.csd.oop.DBMS.util.Regex;
import eg.edu.alexu.csd.oop.DBMS.util.RegexEvaluator;

public class Update extends Query implements Writable {

    Map<String, String> columns;
    private String tableIdentifier;

    public Update() {
        super();
        this.columns = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    private boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_UPDATE);
        if (App.checkForExistence(groups)) {
            this.extractTable(groups[1].trim());
            return this.fillColumns(groups[2].trim());
        }
        return false;
    }

    @Override
    public void execute(DBMS dbms) throws RuntimeException {
        dbms.updateTable(this.getTableIdentifier(), this.getColumns());
    }

    private String[] extractColumns(String s) {
        if (!s.matches(Regex.PARSE_WITH_UPDATE_TRIM_MATCH))
            return null;
        if (s.split(Regex.PARSE_WITH_UPDATE_SPLIT_PATTERN1).length <= s
                .split(Regex.PARSE_WITH_UPDATE_SPLIT_PATTERN2).length)
            return s.split(Regex.PARSE_WITH_UPDATE_SPLIT_PATTERN1);
        else
            return s.split(Regex.PARSE_WITH_UPDATE_SPLIT_PATTERN2);
    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
    }

    private boolean fillColumns(String s) {
        String[] temp;
        String[] columns = this.extractColumns(s.trim());
        if (!App.checkForExistence(columns))
            return false;
        for (int i = 0; i < columns.length; i++) {
            temp = this.isOnForm(columns[i].trim());
            if (App.checkForExistence(temp)) {
                this.columns.put(temp[1].trim(), temp[2].trim());
            } else
                return false;
        }
        return true;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public String getTableIdentifier() {
        return tableIdentifier;
    }

    private String[] isOnForm(String s) {
        if (s.trim().matches(Regex.PARSE_WITH_UPDATE_TRIM_MATCH_LEFT)) {
            String[] check = RegexEvaluator.evaluate(s.trim(),
                    Regex.PARSE_WITH_UPDATE_SPLIT_PATTERN_LEFT);
            if (!App.checkForExistence(check) || !App.isLegalIdentifier(check[1].trim())) {
                return null;
            } else {
                return check;
            }
        } else if (s.trim().matches(Regex.PARSE_WITH_UPDATE_TRIM_MATCH_RIGHT)) {
            String[] check = RegexEvaluator.evaluate(s.trim(),
                    Regex.PARSE_WITH_UPDATE_SPLIT_PATTERN_RIGHT);
            if (!App.checkForExistence(check) || !App.isLegalIdentifier(check[2].trim())) {
                return null;
            } else {
                return (String[]) App.swapArrayOfTwo(check);
            }
        } else {
            return null;
        }
    }

    @Override
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            throw new ParseException("Syntax error!(not a valid update statement)", 0);
    }
}
