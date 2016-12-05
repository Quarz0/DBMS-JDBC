package model.statements;

import controller.DBMS;

public interface Executable {

    public void execute(DBMS dbms) throws RuntimeException;
}
