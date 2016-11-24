package model.statements;

import java.util.ArrayList;
import java.util.List;

import util.RegexEvaluator;

public class Select implements Query {

    private List<String> columns;
    private String table;
    
    public Select() {
        columns = new ArrayList<>();
    }
    
    public Select(String s) {
        this();
        parse(s);
    }
    
    @Override
    public void parse(String s) {
        String pattern = "(.*?)(\\s+)(FROM)(\\s+)(.*)";
        String validTableNamePattern = "^[a-zA-Z_][a-zA-Z0-9_]*$";
                
        String g[] = RegexEvaluator.evaluate(s, pattern);
        String columnsList = g[1].trim();
        String table = g[5].trim();
        
        if (!RegexEvaluator.isMatch(table, validTableNamePattern)) {
            throw new RuntimeException("INVALID TABLE NAME");
        }
        String[] columns = columnsList.split(",");
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].trim().length() == 0) {
                throw new RuntimeException();
            }
            this.columns.add(columns[i].trim());
        }
        return;
    }
    
    public void addColumn(String col) {
        columns.add(col);
    }
    
    public void setTable(String table) {
        this.table = table;
    }
    
    public String getTable() {
        return table;
    }
    
    public List<String> getColumns() {
        return columns;
    }
    
}
