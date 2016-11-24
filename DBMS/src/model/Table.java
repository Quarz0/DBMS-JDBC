package model;

import java.util.List;

public class Table {
    private List<Column<?>> columnsList;
    private List<String> header;
    private ColumnBuilder columnBuilder;

    public Table(List<String> colName, List<Class<?>> types) {
        header = colName;
        for (int i = 0; i < types.size(); i++) {
            Column temp = columnBuilder.buildColumn(types.get(i), colName.get(i));
            columnsList.add(temp);
        }
    }
}
