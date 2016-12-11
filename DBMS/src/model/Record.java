package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
        if (obj == this)
            return true;
        if (!(obj instanceof Record)) {
            return false;
        }

        Record record = (Record) obj;
        return new EqualsBuilder().append(this.getColumns(), record.getColumns())
                .append(this.getValues(), record.getValues()).isEquals();
    }

    public Map<String, Class<?>> getColumns() {
        return columns;
    }

    public List<Object> getValues() {
        return values;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.columns).append(this.values).toHashCode();
    }

}