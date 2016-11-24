package model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Column<?>> columnsList;
    private List<String> header;

    public Table(List<String> colName, List<Class<?>> types) {
        header = colName;
        columnsList = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            Column temp = ColumnBuilder.buildColumn(types.get(i), colName.get(i));
            columnsList.add(temp);
        }
    }

    public Column<?> getColumn(int index) {
        return columnsList.get(index);
    }

    public Class<?> getColumnType(int index) {
        return columnsList.get(index).getType();
    }
}
