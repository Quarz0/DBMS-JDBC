package eg.edu.alexu.csd.oop.DBMS.model.statements.clauses;

import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMSClause;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Clause;
import eg.edu.alexu.csd.oop.DBMS.util.App;
import eg.edu.alexu.csd.oop.DBMS.util.Regex;
import eg.edu.alexu.csd.oop.DBMS.util.RegexEvaluator;

public class Order extends Clause {

    private Map<String, String> columns;

    public Order() {
        super();
        this.columns = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
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
            String[] column = cols[i].trim().split(" +");
            if (!App.isLegalIdentifier(column[0])) {
                return false;
            }
            if (column.length == 1) {
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
            throw new ParseException("Syntax error!(not a valid order clause)", 0);
        }
    }

}
