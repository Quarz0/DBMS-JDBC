package tester;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import model.Table;

public class Test {

    @org.junit.Test
    public void test() {
        List<String> colNames = new ArrayList<>();
        colNames.add("0");
        colNames.add("1");
        colNames.add("2");
        colNames.add("3");
        colNames.add("4");
        colNames.add("5");
        colNames.add("6");
        colNames.add("7");
        List<Class<?>> colTypes = new ArrayList<>();
        colTypes.add(Integer.class);
        colTypes.add(String.class);
        colTypes.add(Double.class);
        colTypes.add(Float.class);
        colTypes.add(Short.class);
        colTypes.add(Byte.class);
        colTypes.add(Character.class);
        colTypes.add(Long.class);
        Table testTable = new Table("testTable", "noDirec", colNames, colTypes);
        assertEquals(Integer.class, testTable.getColumn(0).getType());
        assertEquals(String.class, testTable.getColumn(1).getType());
        assertEquals(Double.class, testTable.getColumn(2).getType());
        assertEquals(Float.class, testTable.getColumn(3).getType());
        assertEquals(Short.class, testTable.getColumn(4).getType());
        assertEquals(Byte.class, testTable.getColumn(5).getType());
        assertEquals(Character.class, testTable.getColumn(6).getType());
        assertEquals(Long.class, testTable.getColumn(7).getType());
    }

}
