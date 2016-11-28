package model;

import java.util.List;

public class Record {
    private List<Object> values;
    private List<String> columns;

    public Record(List<String> columns, List<Object> values) {
        this.columns = columns;
        this.values = values;
    }

    public Record(List<Object> values) {
        this.values = values;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Object> getValues() {
        return values;
    }

    public void addToRecord(Object obj) {
        values.add(obj);
    }
}