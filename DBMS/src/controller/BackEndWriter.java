package controller;

import java.io.FileNotFoundException;

import model.SelectionTable;
import model.Table;

public interface BackEndWriter {

    public SelectionTable readTable(Table table) throws FileNotFoundException;

    public void writeTable(SelectionTable selectionTable, Table table) throws FileNotFoundException;
}
