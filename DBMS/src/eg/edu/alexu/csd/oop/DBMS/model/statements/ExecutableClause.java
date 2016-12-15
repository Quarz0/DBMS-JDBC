package eg.edu.alexu.csd.oop.DBMS.model.statements;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMSClause;

public interface ExecutableClause {

    public void execute(DBMSClause dbms) throws RuntimeException;

}
