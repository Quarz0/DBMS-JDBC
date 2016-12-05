package model.statements;

import java.util.List;

import model.Pair;

public class Order implements Clause {

    private String expression;
    private List<Pair<String, Boolean>> columns;
    
    public Order(String s) {
        this.expression = s;
    }

    public String getExpression() {
        return this.expression;
    }
    
    public List<Pair<String, Boolean>> getColumns(){
        return this.columns;
    }

    public void setExpression(String exp) {
        String[] cols = exp.trim().split(",");
        for (int i = 0; i < cols.length; i++) {
            String[] column = cols[i].trim().split(" ");
            this.columns.add(new Pair<String, Boolean>(column[0], column.length == 1 || column[1].equals("ASC")));
        }
    }

}
