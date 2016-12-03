package model;

import java.util.ArrayList;
import java.util.List;

public class Column<T> {
    private String colName;
    private List<T> column;
    private Class<?> type;

    public Column(String colName, Class<?> cls) {
        this.colName = colName;
        this.type = cls;
        column = new ArrayList<>();
    }

    public void addData(Object data) {
        column.add((T) data);
    }

    public String getColName() {
        return colName;
    }

    public T getData(int index) {
        return column.get(index);
    }

    public Class<?> getType() {
        return type;
    }
}
