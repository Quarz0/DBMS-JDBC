package model;

public class ColumnBuilder {

    public static <cls> Column<cls> buildColumn(Class<?> cls, String colName) {
        return new Column<cls>(colName, cls);
    }

}
