package controller.backEnd;

import java.util.List;
import java.util.Map;

public class JSONTable {
    List<String> colNames;
    String tableName;
    List<String> types;
    List<Map<String, String>> values;

    public JSONTable(String tableName, List<String> names, List<String> type,
            List<Map<String, String>> val) {
        this.tableName = tableName;
        colNames = names;
        types = type;
        values = val;
    }

    public List<String> getColumnsNames() {
        return colNames;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<Map<String, String>> getValues() {
        return values;
    }

}