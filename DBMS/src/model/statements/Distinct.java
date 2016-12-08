package model.statements;

import java.text.ParseException;

import controller.DBMSClause;

public class Distinct extends Clause {
    
    public Distinct() {
        super();
    }
    
    @Override
    public void parse(String s) throws ParseException {
        return;
    }

    @Override
    public void execute(DBMSClause dbms) throws RuntimeException {
        dbms.distinct();
    }

}
