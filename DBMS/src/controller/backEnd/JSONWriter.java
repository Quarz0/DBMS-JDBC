package controller.backEnd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.ClassFactory;
import model.Record;
import model.SelectionTable;
import model.Table;

public class JSONWriter implements BackEndWriter {

    private static final String DATA_FILE_EXTENSION = ".json";
    private static final String VALIDATOR_FILE_EXTENSION = ".jsonv";
    private Gson gson;
    private BufferedReader reader;
    private FileWriter writer;

    public JSONWriter() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public String getDataFileExtension() {
        return DATA_FILE_EXTENSION;
    }

    @Override
    public String getValidatorFileExtension() {
        return VALIDATOR_FILE_EXTENSION;
    }

    @Override
    public File makeDataFile(String tablePath, String tableName, Map<String, Class<?>> header) {
        File jsonFile = new File(tablePath + File.separator + tableName + ".json");
        List<String> names = new ArrayList<>();
        List<String> types = new ArrayList<>();
        for (String name : header.keySet()) {
            names.add(name);
            types.add(header.get(name).getSimpleName());
        }
        try {
            jsonFile.createNewFile();
            writer = new FileWriter(jsonFile);
            List<Map<String, String>> vals = new ArrayList<>();
            JSONTable jsonTable = new JSONTable(tableName, names, types, vals);
            writer.write(gson.toJson(jsonTable));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create data file");
        }
        return jsonFile;
    }

    @Override
    public File makeValidatorFile(String tablePath, String tableName,
            Map<String, Class<?>> header) {
        File validatorFile = new File(tablePath + File.separator + tableName + ".jsonv");
        try {
            validatorFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create validator file");
        }
        return validatorFile;
    }

    @Override
    public SelectionTable readTable(Table table) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(table.getData()));
        JSONTable jsonTable = gson.fromJson(reader, JSONTable.class);
        List<String> colNames = jsonTable.getColumnsNames();
        List<String> types = jsonTable.getTypes();
        List<Map<String, String>> records = jsonTable.getValues();
        Map<String, Class<?>> header = new LinkedHashMap<>();
        for (int i = 0; i < colNames.size(); i++) {
            header.put(colNames.get(i), ClassFactory.getClass(types.get(i)));
        }
        SelectionTable selectionTable = new SelectionTable(jsonTable.getTableName(), header);
        for (int i = 0; i < records.size(); i++) {
            List<Object> values = new ArrayList<>();
            Map<String, String> currRecord = records.get(i);
            for (String name : currRecord.keySet()) {
                if (currRecord.get(name) == "null")
                    values.add(null);
                else
                    values.add(currRecord.get(name));
            }
            selectionTable.addRecord(new Record(header, values));
        }
        return selectionTable;
    }

    @Override
    public void writeTable(SelectionTable selectionTable) throws FileNotFoundException {
        File jsonFile = selectionTable.getTableSchema().getData();
        List<String> colNames = new ArrayList<>();
        List<String> types = new ArrayList<>();
        for (String name : selectionTable.getHeader().keySet()) {
            colNames.add(name);
            types.add(selectionTable.getHeader().get(name).getSimpleName());
        }
        List<Map<String, String>> records = new ArrayList<>();
        for (int i = 0; i < selectionTable.getRecordList().size(); i++) {
            Map<String, String> tempRecord = new LinkedHashMap<>();
            Record currentRecord = selectionTable.getRecordList().get(i);
            for (int j = 0; j < currentRecord.getValues().size(); j++) {
                Object val = currentRecord.getValues().get(j);
                if (val == null)
                    tempRecord.put(colNames.get(j), "null");
                else
                    tempRecord.put(colNames.get(j), val.toString());
            }
            records.add(tempRecord);
        }
        try {
            writer = new FileWriter(jsonFile);
            JSONTable jsonTable = new JSONTable(selectionTable.getTableName(), colNames, types,
                    records);
            writer.write(gson.toJson(jsonTable));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}