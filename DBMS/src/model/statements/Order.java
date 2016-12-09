package model.statements;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import controller.DBMSClause;
import util.App;
import util.Regex;
import util.RegexEvaluator;

public class Order extends Clause {

    private Map<String, String> columns;

    public Order() {
        super();
        this.columns = new LinkedHashMap<>();
    }

    public boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_ORDER_BY_PATTERN);
        if (App.checkForExistence(groups)) {
            return this.extractColIdentifiers(groups[1]);
        }
        return false;
    }

    @Override
    public void execute(DBMSClause dbms) throws RuntimeException {
        dbms.order(this.getColumns());
    }

    private boolean extractColIdentifiers(String s) {
        String[] cols = s.split(",");
        for (int i = 0; i < cols.length; i++) {
            String[] column = cols[i].split(" +");
            if (!App.isLegalIdentifier(column[0])) {
                return false;
            }
            if (column.length == 0) {
                this.columns.put(column[0].trim(), "ASC");
            } else {
                this.columns.put(column[0].trim(), column[1].trim());
            }
        }
        return true;
    }

    public Map<String, String> getColumns() {
        return this.columns;
    }

    @Override
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s) || !this.checkRegex(s)) {
            throw new ParseException("Invalid", 0);
        }
    }

}
