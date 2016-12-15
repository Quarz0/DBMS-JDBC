package eg.edu.alexu.csd.oop.DBMS.model.statements.clauses;

import java.text.ParseException;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMSClause;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Clause;

public class Distinct extends Clause {

    public Distinct() {
        super();
    }

    @Override
    public void execute(DBMSClause dbms) throws RuntimeException {
        dbms.distinct();
    }

    @Override
    public void parse(String s) throws ParseException {
        return;
    }

}
