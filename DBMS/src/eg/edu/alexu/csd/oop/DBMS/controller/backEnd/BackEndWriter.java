package eg.edu.alexu.csd.oop.DBMS.controller.backEnd;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import eg.edu.alexu.csd.oop.DBMS.model.SelectionTable;
import eg.edu.alexu.csd.oop.DBMS.model.Table;

public interface BackEndWriter {

    public String getDataFileExtension();

    public String getValidatorFileExtension();

    public File makeDataFile(String tablePath, String tableName, Map<String, Class<?>> header)
            throws IOException;

    public File makeValidatorFile(String tablePath, String tableName, Map<String, Class<?>> header)
            throws IOException;

    public SelectionTable readTable(Table table) throws IOException;

    public void writeTable(SelectionTable selectionTable) throws IOException;

}
