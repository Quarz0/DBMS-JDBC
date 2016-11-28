package model.statements;

import java.util.ArrayList;
import java.util.List;

import model.Pair;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Update implements Query {

    private String tableIdentifier;
    private List<Pair<String, String>> columns;
    private boolean isAll;
    private Where where;

    public Update() {
        this.columns = new ArrayList<>();
        this.isAll = true;
    }

    @Override
    public void parse(String s) {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            this.callForFailure();
    }

    private boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_UPDATE);
        if (App.checkForExistence(groups)) {
            this.extractTable(groups[1].trim());
            this.fillColumns(groups[2].trim());
            return true;
        }
        return false;
    }

    private void fillColumns(String s) {
        Pair<String, String> pair = null;
        String[] temp;
        String[] columns = this.extractColumns(s.trim());
        if (App.checkForExistence(columns))
            this.callForFailure();
        for (int i = 0; i < columns.length; i++) {
            temp = this.isOnForm(columns[i].trim());
            if (App.checkForExistence(temp))
                pair = new Pair<String, String>(temp[0], temp[1]);
            else
                this.callForFailure();
            this.columns.add(pair);
        }
    }

    private String[] extractColumns(String s) {
        if (!s.matches(Regex.PARSE_WITH_UPDATE_TRIM_MATCH))
            this.callForFailure();
        if (s.split(Regex.PARSE_WITH_UPDATE_SPLIT_PATTERN1).length <= s
                .split(Regex.PARSE_WITH_UPDATE_SPLIT_PATTERN2).length)
            return s.split(Regex.PARSE_WITH_UPDATE_SPLIT_PATTERN1);
        else
            return s.split(Regex.PARSE_WITH_UPDATE_SPLIT_PATTERN2);
    }

    private String[] isOnForm(String s) {
        if (s.trim().matches(Regex.PARSE_WITH_UPDATE_TRIM_MATCH_LEFT)) {
            String[] check = RegexEvaluator.evaluate(s.trim(),
                    Regex.PARSE_WITH_UPDATE_SPLIT_PATTERN_LEFT);
            if (!App.checkForExistence(check) || !App.isColumnIdentifier(check[1].trim())) {
                return null;
            } else {
                return check;
            }
        } else if (s.trim().matches(Regex.PARSE_WITH_UPDATE_TRIM_MATCH_RIGHT)) {
            String[] check = RegexEvaluator.evaluate(s.trim(),
                    Regex.PARSE_WITH_UPDATE_SPLIT_PATTERN_RIGHT);
            if (!App.checkForExistence(check) || !App.isColumnIdentifier(check[2].trim())) {
                return null;
            } else {
                return (String[]) App.swapArrayOfTwo(check);
            }
        } else {
            return null;
        }
    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
    }

    private void callForFailure(/* Exception e */) {

    }

    public String getTableIdentifier() {
        return tableIdentifier;
    }

    public List<Pair<String, String>> getColumns() {
        return columns;
    }

    public boolean isAll() {
        return isAll;
    }

    public Where getWhere() {
        return where;
    }

    @Override
    public void setClause(Clause clause) {
        if (clause instanceof Where)
            this.where = (Where) clause;
        if (!App.checkForExistence(this.where))
            this.isAll = true;
        else
            this.isAll = false;
    }

}
