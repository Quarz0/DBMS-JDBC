package eg.edu.alexu.csd.oop.DBMS.model.statements;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMS;

public interface ExecutableQuery {

    public void execute(DBMS dbms) throws RuntimeException;
}
