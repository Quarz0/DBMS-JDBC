package model.statements;

import controller.DBMS;

public interface ExecutableQuery {

    public void execute(DBMS dbms) throws RuntimeException;
}
