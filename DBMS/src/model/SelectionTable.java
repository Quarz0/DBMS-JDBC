package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthLongestWordMaxCol;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;

public class SelectionTable implements Cloneable {
    private Map<String, Class<?>> header;
    private Map<String, Class<?>> defaultHeader;

    private List<Record> recordList;
    private String tableName;
    private Table tableSchema;

    public SelectionTable(String tableName, Map<String, Class<?>> colNames) {
        this(tableName);
        this.header = colNames;
        defaultHeader = new Hashtable<>();
        for (Entry<String, Class<?>> entry : header.entrySet()) {
            defaultHeader.put(entry.getKey().toLowerCase(), entry.getValue());
        }
    }

    public SelectionTable(String tableName) {
        this.recordList = new ArrayList<>();
        this.tableName = tableName;
        this.tableSchema = null;
    }

    public void addRecord(Record record) {
        recordList.add(record);
    }

    public Map<String, Class<?>> getHeader() {
        return header;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {

        V2_AsciiTable asciiTable = new V2_AsciiTable();
        asciiTable.addStrongRule();
        asciiTable.addRow(this.header.keySet().toArray());
        asciiTable.addStrongRule();

        for (int i = 0; i < recordList.size(); i++) {
            asciiTable.addRow(recordList.get(i).getValues().toArray());
        }
        asciiTable.addRule();

        V2_AsciiTableRenderer asciiTableRenderer = new V2_AsciiTableRenderer();
        asciiTableRenderer.setTheme(V2_E_TableThemes.PLAIN_7BIT.get());
        asciiTableRenderer.setWidth(new WidthLongestWordMaxCol(40));
        RenderedTable renderedTable = asciiTableRenderer.render(asciiTable);
        return "  Table: " + this.tableName + "\n" + renderedTable.toString() + "  Records: "
                + this.recordList.size() + "\n\n";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        SelectionTable selectionTable = new SelectionTable(tableName, copyHeader());
        for (Iterator<Record> iterator = recordList.iterator(); iterator.hasNext();) {
            Record object = (Record) iterator.next();
            selectionTable.addRecord((Record) object.clone());
        }
        return selectionTable;
    }

    public Map<String, Class<?>> getDefaultHeader() {
        return defaultHeader;
    }

    private Map<String, Class<?>> copyHeader() {
        Map<String, Class<?>> result = new LinkedHashMap<>();
        for (Iterator<String> iterator = header.keySet().iterator(); iterator.hasNext();) {
            String record = (String) iterator.next();
            result.put(record, header.get(record));
        }
        return result;
    }

    public Table getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(Table tableSchema) {
        this.tableSchema = tableSchema;
    }

}
