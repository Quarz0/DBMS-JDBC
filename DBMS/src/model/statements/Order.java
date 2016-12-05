package model.statements;

import java.text.ParseException;
import java.util.List;

import controller.DBMSClause;
import model.Pair;

public class Order extends Clause {

    private String expression;
    private List<Pair<String, Boolean>> columns;

    public Order(String s) {
        this.expression = s;
    }

    public String getExpression() {
        return this.expression;
    }

    public List<Pair<String, Boolean>> getColumns() {
        return this.columns;
    }

    public void setExpression(String exp) {
        String[] cols = exp.trim().split(",");
        for (int i = 0; i < cols.length; i++) {
            String[] column = cols[i].trim().split(" ");
            this.columns.add(new Pair<String, Boolean>(column[0],
                    column.length == 1 || column[1].equals("ASC")));
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
