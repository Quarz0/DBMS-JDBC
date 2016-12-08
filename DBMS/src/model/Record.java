package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Record implements Cloneable {
    private Map<String, Class<?>> columns;
    private List<Object> values;

    public Record(Map<String, Class<?>> columns, List<Object> values) {
        this.columns = columns;
        this.values = values;
    }

    public Record() {
        values = new ArrayList<>();
    }

    public void addToRecord(Object obj) {
        values.add(obj);
    }

    public Map<String, Class<?>> getColumns() {
        return columns;
    }

    public List<Object> getValues() {
        return values;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Record(copyHeader(), copyValues());
    }

    private List<Object> copyValues() {
        List<Object> newValues = new ArrayList<>();
        for (Iterator<Object> iterator = values.iterator(); iterator.hasNext();) {
            Object object = (Object) iterator.next();
            newValues.add(object);
        }
        return newValues;
    }

    private Map<String, Class<?>> copyHeader() {
        Map<String, Class<?>> result = new LinkedHashMap<>();
        for (Iterator<String> iterator = columns.keySet().iterator(); iterator.hasNext();) {
            String record = (String) iterator.next();
            result.put(record, columns.get(record));
        }
        return result;
    }

}