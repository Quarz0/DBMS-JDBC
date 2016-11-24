package model;

import java.util.ArrayList;
import java.util.List;

public class Column<T> {
    private List<T> column;
    private String colName;

    public Column(String colName) {
        this.colName = colName;
        column = new ArrayList<>();
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
