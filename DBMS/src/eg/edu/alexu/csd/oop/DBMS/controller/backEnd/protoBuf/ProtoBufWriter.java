package eg.edu.alexu.csd.oop.DBMS.controller.backEnd.protoBuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.BackEndWriter;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.protoBuf.protoBufDependecies.PBWriter.PBSelectionTable;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.protoBuf.protoBufDependecies.PBWriter.PBSelectionTable.PBRecord;
import eg.edu.alexu.csd.oop.DBMS.model.Record;
import eg.edu.alexu.csd.oop.DBMS.model.SelectionTable;
import eg.edu.alexu.csd.oop.DBMS.model.Table;
import eg.edu.alexu.csd.oop.DBMS.model.TypeFactory;
import eg.edu.alexu.csd.oop.DBMS.util.App;

public class ProtoBufWriter implements BackEndWriter {
    private static final String DATA_FILE_EXTENSION = ".ser";
    private static final String VALIDATOR_FILE_EXTENSION = ".vser";

    private List<String> classesToStrings(Collection<Class<?>> types) {
        List<String> ret = new ArrayList<>();
        for (Class<?> type : types) {
            ret.add(type.getSimpleName());
        }
        return ret;
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
    public File makeDataFile(String tablePath, String tableName, Map<String, Class<?>> header)
            throws IOException {
        PBSelectionTable.Builder pbSelectionBuilder = PBSelectionTable.newBuilder();
        pbSelectionBuilder.addAllColNames(header.keySet());
        pbSelectionBuilder.addAllTypes(classesToStrings(header.values()));
        pbSelectionBuilder.setTableName(tableName);
        File dataFile = new File(tablePath + File.separator + tableName + DATA_FILE_EXTENSION);
        dataFile.createNewFile();
        FileOutputStream writer = new FileOutputStream(dataFile);
        pbSelectionBuilder.build().writeTo(writer);
        return dataFile;
    }

    @Override
    public File makeValidatorFile(String tablePath, String tableName, Map<String, Class<?>> header)
            throws IOException {
        File validatorFile = new File(
                tablePath + File.separator + tableName + VALIDATOR_FILE_EXTENSION);
        validatorFile.createNewFile();
        return null;
    }

    @Override
    public SelectionTable readTable(Table table) throws FileNotFoundException, IOException {
        PBSelectionTable tempPBSeletionTable = PBSelectionTable
                .parseFrom(new FileInputStream(table.getData()));
        Iterable<String> colNames = tempPBSeletionTable.getColNamesList();
        Iterable<String> types = tempPBSeletionTable.getTypesList();
        Iterator<String> colNameIt = colNames.iterator();
        Iterator<String> typesAsStringsIt = types.iterator();
        Map<String, Class<?>> header = new LinkedHashMap<>();
        while (colNameIt.hasNext() && typesAsStringsIt.hasNext()) {
            header.put(colNameIt.next(), TypeFactory.getClass(typesAsStringsIt.next()));
        }
        SelectionTable selectedTable = new SelectionTable(tempPBSeletionTable.getTableName(),
                header);
        for (PBRecord pbRecord : tempPBSeletionTable.getRecordListList()) {
            List<Object> values = new ArrayList<>();
            Iterator<String> valuesIt = pbRecord.getValuesList().iterator();
            Iterator<Class<?>> typeIt = header.values().iterator();
            while (typeIt.hasNext() && valuesIt.hasNext()) {
                Class<?> cls = typeIt.next();
                String val = valuesIt.next();
                if (val.equals("NULL"))
                    values.add(null);
                else
                    values.add(TypeFactory.parseToObject(cls, App.addQuotes(cls, val)));
            }
            selectedTable.addRecord(new Record(header, values));
        }
        return selectedTable;
    }

    @Override
    public void writeTable(SelectionTable selectionTable) throws IOException {
        PBSelectionTable.Builder pbSelectionTableBuilder = PBSelectionTable.newBuilder();
        pbSelectionTableBuilder.setTableName(selectionTable.getTableName());
        pbSelectionTableBuilder.addAllColNames(selectionTable.getHeader().keySet());
        pbSelectionTableBuilder.addAllTypes(classesToStrings(selectionTable.getHeader().values()));
        List<PBRecord> recordList = new ArrayList<>();
        for (Record record : selectionTable.getRecordList()) {
            PBRecord.Builder pbRecordBuilder = PBRecord.newBuilder();
            for (Object value : record.getValues()) {
                pbRecordBuilder.addValues(App.checkForExistence(value) ? value.toString() : "NULL");
            }
            recordList.add(pbRecordBuilder.build());
        }
        pbSelectionTableBuilder.addAllRecordList(recordList);
        FileOutputStream writer = new FileOutputStream(selectionTable.getTableSchema().getData());
        pbSelectionTableBuilder.build().writeTo(writer);
        writer.close();
    }

}
