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

    public List<Column<?>> tester() {
        return columnsList;
    }
}
