package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.CompareToBuilder;

import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthLongestWordMaxCol;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;

public class SelectionTable {
    private List<String> header;
    private List<Record> recordList;
    private String tableName;

    public SelectionTable(List<String> colNames) {
        header = colNames;
        recordList = new ArrayList<>();
    }

    public SelectionTable(String tableName, List<String> colNames) {
        header = colNames;
        this.tableName = tableName;
        recordList = new ArrayList<>();
    }

    public void addRecord(Record record) {
        recordList.add(record);
    }

    public List<String> getHeader() {
        return header;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public String getTableName() {
        return tableName;
    }
    
//    public List<Record> getDistinct(){
//        List<Record> distinctList = new ArrayList<>();
//        for (int i = 0; i < this.recordList.size(); i++) {
//            Record curRecord = this.recordList.get(i);
//            
//            for (int j = 0; j < distinctList.size(); j++) {
//                boolean equal = true;
//                for (int k = 0; k < curRecord.getValues().size(); k++) {
//                    equal = equal && curRecord.getValues().get(k).equals(distinctList.get(j).getValues().get(k));
//                }
//                if (equal) {
//                    distinctList.add(curRecord);
//                    break;
//                }
//            }
//        }
//        
//
//        
//        
//        return distinctList;
//    }
    
//  Collections.sort(recordList, new Comparator<Record>() {  
//  
//  List<Pair<String, Boolean>> orderColumns;
//  Map<String, Integer> columnsMap;
//  
//  @Override
//  public int compare(Record r1, Record r2) {
//      CompareToBuilder compare = new CompareToBuilder();
//      for (int i = 0; i < orderColumns.size(); i++) {
//          int index = columnsMap.get(orderColumns.get(i).getFirst());
//          if (orderColumns.get(i).getSecond())
//              compare.append(r1.getValues().get(index), r2.getValues().get(index));
//          else
//              compare.append(r2.getValues().get(index), r1.getValues().get(index));
//      }
//      return compare.toComparison();
//  }  
//}); 
    
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

}
