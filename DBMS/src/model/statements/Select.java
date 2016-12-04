package model.statements;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import controller.DBMS;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Select extends Query {

    private List<String> columns;
    private boolean isAll;
    private String tableIdentifier;
    private Where where;

    public Select() {
        super();
        this.columns = new ArrayList<>();
        this.isAll = false;
    }

    public void addColumn(String col) {
        columns.add(col);
    }

    public boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_SELECT_ALL_FROM);
        if (App.checkForExistence(groups)) {
            this.extractTable(groups[1].trim());
            this.isAll = true;
            return true;
        } else {
            groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_SELECT_FROM);
            if (App.checkForExistence(groups)) {
                this.extractTable(groups[2].trim());
                return this.extractColIdentifiers(groups[1].trim());
            }
        }
        return false;
    }

    @Override
    public void execute(DBMS dbms) throws RuntimeException {
        // TODO Auto-generated method stub

    }

    private boolean extractColIdentifiers(String s) {
        String[] colmuns = s.split(",");
        String tableName;
        for (int i = 0; i < colmuns.length; i++) {
            tableName = colmuns[i].trim();
            if (RegexEvaluator.isMatch(tableName, Regex.LEGAL_IDENTIFIER)) {
                this.columns.add(tableName);
            } else {
                return false;
            }
        }
        return true;
    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
    }

    public List<String> getColumns() {
        return this.columns;
    }

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public Where getWhere() {
        return where;
    }

    public boolean isAll() {
        return isAll;
    }

    @Override
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            throw new ParseException("Invalid", 0);
    }

}
