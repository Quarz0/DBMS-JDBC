package model;

import java.util.List;

public class ColumnBuilder {

    public static <cls> Column buildColumn(Class<?> cls, String colName) {
        return new Column<cls>(colName, cls);
    }

}
