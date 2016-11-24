package model;

import java.util.List;

public class Table {
    private List<Column<?>> columnsList;
    private List<String> header;

    public Table(List<String> colName, List<Class<?>> types) {
        header = colName;
        for (int i = 0; i < values.size(); i++) {
            Column<types.get(i).class> temp = new Column<>(colName.get(i));
            columnsList.add(temp);
        }
    }
}
