package model.statements;

import java.text.ParseException;

import controller.DBMS;

public class Where extends Clause {

    private String expression;

    public Where(String s) {
        expression = s;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String exp) {
        expression = exp;
    }

    @Override
    public void execute(DBMS dbms) throws RuntimeException {
        // TODO Auto-generated method stub

    }

    @Override
    public void parse(String s) throws ParseException {
        // TODO Auto-generated method stub

    }

}
