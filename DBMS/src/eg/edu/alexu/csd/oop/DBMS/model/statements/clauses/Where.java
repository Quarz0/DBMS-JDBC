package eg.edu.alexu.csd.oop.DBMS.model.statements.clauses;

import java.text.ParseException;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMSClause;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Clause;
import eg.edu.alexu.csd.oop.DBMS.util.App;

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
            throw new ParseException("Syntax error!(not a valid where clause)", 0);
        }
        this.setExpression(s);
    }

    public void setExpression(String exp) {
        this.expression = exp;
    }

}
