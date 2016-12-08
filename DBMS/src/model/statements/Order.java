package model.statements;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import controller.DBMSClause;
import model.Pair;

public class Order extends Clause {

    private String expression;
    private Map<String, String> columns;

    public Order(String s) {
        this.expression = s;
    }

    public String getExpression() {
        return this.expression;
    }

    public Map<String, String> getColumns() {
        return this.columns;
    }

    public void setExpression(String exp) {
        String[] cols = exp.trim().split(",");
        for (int i = 0; i < cols.length; i++) {
            String[] column = cols[i].trim().split(" ");
            this.columns.put(column[0], column[1]);
        }
    }

    @Override
    public void parse(String s) throws ParseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute(DBMSClause dbms) throws RuntimeException {
        // TODO Auto-generated method stub

    }

}
