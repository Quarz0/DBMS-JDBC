package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Column<?>> columnsList;
    private List<String> header;
    private String tableName;
    private String tableDir;
    private File tableFile;

    public Table(String tableName, String tableDirc, List<String> colNames, List<Class<?>> types) {
        header = colNames;
        this.tableDir = tableDirc;
        this.tableName = tableName + tableName;
        createTableDir();
        columnsList = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            Column<?> temp = ColumnBuilder.buildColumn(types.get(i), colNames.get(i));
            columnsList.add(temp);
        }
    }

    private void createTableDir() {
        tableFile = new File(tableDir);
        if (!tableFile.mkdir()) {
            throw new RuntimeException("Cannot create table directory");
        }
        tableDir += File.separator;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getHeader() {
        return header;
    }

    public Column<?> getColumn(int index) {
        return columnsList.get(index);
    }

    public Class<?> getColumnType(int index) {
        return columnsList.get(index).getType();
    }

    public File getTableFile() {
        return tableFile;
    }

    public List<Column<?>> getColumnsList() {
        return columnsList;
    }

    public int containsColumn(String colName) {
        for (int index = 0; index < columnsList.size(); index++) {
            if (columnsList.get(index).getColName().equals(colName))
                return index;
        }
        return -1;
    }
}
