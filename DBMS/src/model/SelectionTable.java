package model;

import java.util.ArrayList;
import java.util.List;

public class SelectionTable {
    private List<Record> recordList;
    private List<String> header;
    private String tableName;

    public SelectionTable(String tableName, List<String> colNames) {
        header = colNames;
        this.tableName = tableName;
        recordList = new ArrayList<>();
    }

    public SelectionTable(List<String> colNames, List<Class<?>> types) {
        header = colNames;
        recordList = new ArrayList<>();
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getHeader() {
        return header;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void addRecord(Record record) {
        recordList.add(record);
    }
}
