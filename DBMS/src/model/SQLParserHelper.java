package model;

import java.util.ArrayList;
import java.util.List;

import controller.DBMSController;
import model.statements.Query;

public class SQLParserHelper {

    private DBMSController dbmsController;
    private Query currentQuery;
    private List<Observer> observers;

    public SQLParserHelper(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        this.observers = new ArrayList<>();
    }

    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void notifyObservers() {
        this.observers.forEach(Observer::update);
    }

    public Query getCurrentQuery() {
        return currentQuery;
    }

    public void setCurrentQuery(Query currentQuery) {
        this.currentQuery = currentQuery;
        this.notifyObservers();
    }

}
