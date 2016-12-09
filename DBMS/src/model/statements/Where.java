package model.statements;

import java.text.ParseException;

import controller.DBMSClause;
import util.App;

public class Where extends Clause {

    private String expression;

    public Where() {
        super();
    }

    @Override
    public void execute(DBMSClause dbms) throws RuntimeException {
        dbms.whereForDelete(this.getExpression());
        dbms.whereForSelect(this.getExpression());
        dbms.whereForUpdate(this.getExpression());
    }

    public String getExpression() {
        return this.expression;
    }

    @Override
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s)) {
            throw new ParseException("Invalid", 0);
        }
        this.setExpression(s);
    }

    public void setExpression(String exp) {
        this.expression = exp;
    }

}
