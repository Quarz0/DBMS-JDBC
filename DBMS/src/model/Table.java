package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Record> recordList;
    private List<String> header;
    private List<Class<?>> types;
    private String tableName;
    private String tableDir;
    private File tableFile;
    private File tableXMLFile;
    private File tableDTDFile;

    public Table(String tableName, String tableDirc, List<String> colNames, List<Class<?>> types) {
        header = colNames;
        this.tableDir = tableDirc;
        this.tableName = tableName + tableName;
        createTableDir();
        recordList = new ArrayList<>();
    }

    public Table(List<String> colNames, List<Class<?>> types) {
        header = colNames;
        recordList = new ArrayList<>();
    }

    private void createTableDir() {
        tableFile = new File(tableDir);
        if (!tableFile.mkdir()) {
            throw new RuntimeException("Cannot create table directory");
        }
        tableDir += File.separator;
        tableXMLFile = new File(tableDir + tableName + ".xml");
        tableDTDFile = new File(tableDir + tableName + ".dtd");
        try {
            tableXMLFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create " + tableName + ".xml File");
        }
        try {
            tableDTDFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create " + tableName + ".dtd File");
        }
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getHeader() {
        return header;
    }

    public File getTableFile() {
        return tableFile;
    }

    public List<Class<?>> getTypes() {
        return types;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void addRecord(Record record) {
        recordList.add(record);
    }

    public File getXMLFile() {
        return tableXMLFile;
    }

    public File getDTDFile() {
        return tableDTDFile;
    }
}
