package eg.edu.alexu.csd.oop.DBMS.junitTests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.BackEndWriter;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.protoBuf.ProtoBufWriter;
import eg.edu.alexu.csd.oop.DBMS.model.SelectionTable;
import eg.edu.alexu.csd.oop.DBMS.model.Table;

public class TestProtocolBuffer {

    @Test
    public void test() {
        BackEndWriter writer = new ProtoBufWriter();
        String tableName = "Student";
        Map<String, Class<?>> header = new LinkedHashMap<>();
        header.put("ID", Integer.class);
        header.put("Name", String.class);
        header.put("Address", String.class);
        header.put("Birthday", java.sql.Date.class);
        SelectionTable selectedTable = new SelectionTable(tableName, header);
        String tableDir = System.getProperty("user.home") + File.separator + "Desktop";
        Table table = null;
        try {
            table = new Table(new File(tableDir));
            table.registerFiles(writer.makeDataFile(tableDir, tableName, header),
                    writer.makeValidatorFile(tableDir, tableName, header));
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
        SelectionTable readed = null;
        try {
            readed = writer.readTable(table);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(selectedTable.getTableName(), readed.getTableName());
        assertEquals(selectedTable.getHeader(), readed.getHeader());
    }
}
