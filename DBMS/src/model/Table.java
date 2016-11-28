package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthAbsoluteEven;
import de.vandermeer.asciitable.v2.render.WidthFixedColumns;
import de.vandermeer.asciitable.v2.render.WidthLongestLine;
import de.vandermeer.asciitable.v2.render.WidthLongestWordMaxCol;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;

public class Table {
    private List<Column<?>> columnsList;
    private List<String> header;
    private String tableName;
    private String tableDir;
    private File tableFile;

    public Table(String tableName, String tableDirc, List<String> colName, List<Class<?>> types) {
        header = colName;
        this.tableDir = tableDirc;
        this.tableName = tableName;
        // createTableDir();
        columnsList = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            Column<?> temp = ColumnBuilder.buildColumn(types.get(i), colName.get(i));
            columnsList.add(temp);
        }
    }

    private void createTableDir() {
        tableFile = new File(tableDir);
        if (!tableFile.mkdir()) {
            throw new RuntimeException("Cannot create table directory");
        }
        tableDir += File.separator;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getHeader() {
        return header;
    }

    public Column<?> getColumn(int index) {
        return columnsList.get(index);
    }

    public Class<?> getColumnType(int index) {
        return columnsList.get(index).getType();
    }

    @Override
    public String toString() {
        V2_AsciiTable asciiTable = new V2_AsciiTable();
        asciiTable.addStrongRule();
        asciiTable.addRow(this.header.toArray());
        asciiTable.addRule();
        V2_AsciiTableRenderer asciiTableRenderer = new V2_AsciiTableRenderer();
        asciiTableRenderer.setTheme(V2_E_TableThemes.UTF_LIGHT.get());
        asciiTableRenderer.setWidth(new WidthLongestWordMaxCol(70));
        RenderedTable renderedTable = asciiTableRenderer.render(asciiTable);
        return renderedTable.toString();
    }
    // Test
//     public static void main(String[] args) {
//    
//     List<String> col = new ArrayList<>();
//     col.add("Hellooodddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddooooo");
//     col.add("world");
//     List<Class<?>> type = new ArrayList<>();
//     type.add(String.class);
//     type.add(String.class);
//     Table table = new Table("", null, col, type);
//     System.out.println(table.toString());
//     }
}
