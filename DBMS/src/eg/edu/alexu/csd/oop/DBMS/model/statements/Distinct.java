package eg.edu.alexu.csd.oop.DBMS.model.statements;

import java.text.ParseException;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMSClause;

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
