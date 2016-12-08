package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import model.SelectionTable;
import model.Table;

public interface BackEndWriter {

    public SelectionTable readTable(Table table) throws FileNotFoundException;

    public void writeTable(SelectionTable selectionTable, Table table) throws FileNotFoundException;

    public File makeValidatorFile(String tablePath, String tableName, Map<String, Class<?>> header);

    public File makeDataFile(String tablePath, String tableName, Map<String, Class<?>> header);

}
