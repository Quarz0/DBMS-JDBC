package model.statements;

import controller.DBMSClause;

public interface ExecutableClause {

    public void execute(DBMSClause dbms) throws RuntimeException;

}
