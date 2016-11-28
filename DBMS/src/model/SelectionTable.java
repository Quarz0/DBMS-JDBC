package model;

import java.util.ArrayList;
import java.util.List;

import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthFixedColumns;
import de.vandermeer.asciitable.v2.themes.V2_E_RowThemes;
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

    @Override
    public String toString() {
   
        int terminalWidth = jline.TerminalFactory.get().getWidth();
        if (terminalWidth == 1)
            terminalWidth = 100;
        else 
            terminalWidth -= 10;
        
        V2_AsciiTable asciiTable = new V2_AsciiTable();
        asciiTable.addStrongRule();
        asciiTable.addRow(this.header.toArray());
        asciiTable.addStrongRule();

        for (int i = 0; i < recordList.size(); i++) {
            asciiTable.addRow(recordList.get(i).getValues().toArray());
        }
        asciiTable.addRule();
        
        WidthFixedColumns width = new WidthFixedColumns();
        for (int i = 0; i < this.header.size(); i++) {
             width.add(terminalWidth / this.header.size());
        }
        
        V2_AsciiTableRenderer asciiTableRenderer = new V2_AsciiTableRenderer();
        asciiTableRenderer.setTheme(V2_E_TableThemes.PLAIN_7BIT.get());
        asciiTableRenderer.setWidth(width);
        RenderedTable renderedTable = asciiTableRenderer.render(asciiTable);
        return "  Table: " + this.tableName + "\n" + renderedTable.toString() + "  Records: " + this.recordList.size() + "\n";
    }

    // Test
    public static void main(String[] args) {

        List<String> col = new ArrayList<>();
        col.add("Name");
        col.add("Age");
        List<Object> r1 = new ArrayList<>();
        r1.add("marwan");
        r1.add(19);
        Record r = new Record(null, r1);
        
        SelectionTable table = new SelectionTable("Table1", col);
        table.addRecord(r);
        table.addRecord(r);
        System.out.println(table.toString());
        
    }      
}