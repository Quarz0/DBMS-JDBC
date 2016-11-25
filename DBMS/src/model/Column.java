package model;

import java.util.ArrayList;
import java.util.List;

public class Column<T> {
    private List<T> column;
    private String colName;
    private Class<?> type;

    public Column(String colName, Class<?> cls) {
        this.colName = colName;
        this.type = cls;
        column = new ArrayList<>();
    }

    public Class<?> getType() {
        return type;
    }

    public void addData(T data) {
        column.add(data);
    }

    public T getData(int index) {
        return column.get(index);
    }

    public String getColName() {
        return colName;
    }
}
