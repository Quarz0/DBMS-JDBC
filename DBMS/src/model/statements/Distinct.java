package model.statements;

import java.text.ParseException;
import java.util.Map;

import controller.DBMSClause;

public class Distinct extends Clause {

    // TODO
    
    public Distinct() {
        
    }

//    public Map<String, String> getColumns() {
//        return this.columns;
//    }


    @Override
    public void parse(String s) throws ParseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute(DBMSClause dbms) throws RuntimeException {
//        dbms.order(this.getColumns());
    }

}
