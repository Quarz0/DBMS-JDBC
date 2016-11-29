package model;

import java.util.ArrayList;
import java.util.List;

import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthFixedColumns;
import de.vandermeer.asciitable.v2.render.WidthLongestWordMaxCol;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;

public class SelectionTable {
    private List<Record> recordList;
    private List<String> header;
    private String tableName;

    public SelectionTable(String tableName, List<String> colNames) {
        header = colNames;
        this.tableName = tableName;
        recordList = new ArrayList<>();
    }

    public SelectionTable(List<String> colNames) {
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

    @Override
    public String toString() {

        V2_AsciiTable asciiTable = new V2_AsciiTable();
        asciiTable.addStrongRule();
        asciiTable.addRow(this.header.toArray());
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

    // Test
    public static void main(String[] args) {

        List<String> columns = new ArrayList<>();
        columns.add("Name");
        columns.add("Age");
        columns.add("Married");
        List<Object> values = new ArrayList<>();
        values.add("Rob");
        values.add(20);
        values.add(false);
        List<Object> r1 = new ArrayList<>();
        r1.add("marwan");
        r1.add(19);
        Record r = new Record(null, values);
        List<Object> values2 = new ArrayList<>();
        values2.add("Edward");
        values2.add(15);
        values2.add(false);
        Record rr = new Record(null, values2);

        SelectionTable table = new SelectionTable("Table1", columns);
        table.addRecord(r);
        table.addRecord(rr);
        System.out.println(table.toString());

    }
}
