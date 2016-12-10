package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import util.App;

public class Record implements Cloneable {
    private Map<String, Class<?>> columns;
    private List<Object> values;

    public Record(Map<String, Class<?>> columns) {
        this.columns = columns;
        values = new ArrayList<>();
    }

    public Record(Map<String, Class<?>> columns, List<Object> values) {
        this(columns);
        this.values = values;
    }

    public void addToRecord(Object obj) {
        values.add(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Record(copyHeader(), copyValues());
    }

    private Map<String, Class<?>> copyHeader() {
        Map<String, Class<?>> result = new LinkedHashMap<>();
        for (Iterator<String> iterator = columns.keySet().iterator(); iterator.hasNext();) {
            String record = iterator.next();
            result.put(record, columns.get(record));
        }
        return result;
    }

    private List<Object> copyValues() {
        List<Object> newValues = new ArrayList<>();
        for (Iterator<Object> iterator = values.iterator(); iterator.hasNext();) {
            Object object = iterator.next();
            newValues.add(object);
        }
        return newValues;
    }

    @Override
    public boolean equals(Object obj) {
        if (App.checkForExistence(columns) || App.checkForExistence(values)) {
            return columns == values;
        }
        return columns.equals(((Record) obj).getColumns())
                && values.equals(((Record) obj).getValues());
    }

    public Map<String, Class<?>> getColumns() {
        return columns;
    }

    public List<Object> getValues() {
        return values;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (columns == null ? 0 : columns.hashCode());
        result = prime * result + (values == null ? 0 : values.hashCode());
        return result;
    }

}