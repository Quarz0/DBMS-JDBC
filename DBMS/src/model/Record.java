package model;

import java.util.List;

public class Record {
    private List<Object> values;
    private List<String> columns;

    public Record(List<String> columns, List<Object> values) {
        this.columns = columns;
        this.values = values;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Object> getValues() {
        return values;
    }

    public Pair<String, Object> getCell(int index) {
        return new Pair<String, Object>(columns.get(index), values.get(index));
    }
}
