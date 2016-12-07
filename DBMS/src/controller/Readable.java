package controller;

import java.io.FileNotFoundException;

import model.SelectionTable;
import model.Table;

public interface Readable {

    public SelectionTable readTable(Table table) throws FileNotFoundException;
}
