package model;

import java.util.List;

public class Record implements Cloneable {
    private List<String> columns;
    private List<Object> values;

    public Record(List<Object> values) {
        this.values = values;
    }

    public Record(List<String> columns, List<Object> values) {
        this.columns = columns;
        this.values = values;
    }

    public void addToRecord(Object obj) {
        values.add(obj);
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Object> getValues() {
        return values;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}