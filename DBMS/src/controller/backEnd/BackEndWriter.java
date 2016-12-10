package controller.backEnd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import model.SelectionTable;
import model.Table;

public interface BackEndWriter {

    public String getDataFileExtension();

    public String getValidatorFileExtension();

    public File makeDataFile(String tablePath, String tableName, Map<String, Class<?>> header);

    public File makeValidatorFile(String tablePath, String tableName, Map<String, Class<?>> header);

    public SelectionTable readTable(Table table) throws FileNotFoundException;

    public void writeTable(SelectionTable selectionTable) throws FileNotFoundException;

}
