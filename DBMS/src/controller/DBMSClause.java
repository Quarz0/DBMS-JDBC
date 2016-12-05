package controller;

public interface DBMSClause {

    public void whereForDelete(String condition) throws RuntimeException;

    public void whereForUpdate(String condition) throws RuntimeException;

    public void whereForSelect(String condition) throws RuntimeException;
}
