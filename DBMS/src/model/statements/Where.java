package model.statements;

public class Where implements Clause {

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

}
