package controller;

import model.statements.Query;

public interface DBMS {

    public void create(Query query);

    public void drop(Query query);

    public void insertIntoTable(Query query);

    public void updateTable(Query query);

    public void selectFromTable(Query query);

    public void deleteFromTable(Query query);
}
