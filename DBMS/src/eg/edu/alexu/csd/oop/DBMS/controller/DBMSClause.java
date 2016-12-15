package eg.edu.alexu.csd.oop.DBMS.controller;

import java.util.Map;

import eg.edu.alexu.csd.oop.DBMS.model.statements.queries.Select;

public interface DBMSClause {

    public void distinct() throws RuntimeException;

    public void order(Map<String, String> columns) throws RuntimeException;

    public void union(Select select) throws RuntimeException;

    public void whereForDelete(String condition) throws RuntimeException;

    public void whereForSelect(String condition) throws RuntimeException;

    public void whereForUpdate(String condition) throws RuntimeException;
}
