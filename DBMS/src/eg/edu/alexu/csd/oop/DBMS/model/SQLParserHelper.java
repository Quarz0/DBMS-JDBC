package eg.edu.alexu.csd.oop.DBMS.model;

import java.util.ArrayList;
import java.util.List;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMSController;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Query;

public class SQLParserHelper {

    private Query currentQuery;
    @SuppressWarnings("unused")
    private DBMSController dbmsController;
    private List<Observer> observers;

    public SQLParserHelper(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        this.observers = new ArrayList<>();
    }

    public Query getCurrentQuery() {
        return currentQuery;
    }

    public void notifyObservers() throws RuntimeException {
        this.observers.forEach(Observer::update);
    }

    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void setCurrentQuery(Query currentQuery) throws RuntimeException {
        this.currentQuery = currentQuery;
        this.notifyObservers();
    }

}
