package controller.backEnd.xml;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.Record;
import model.SelectionTable;
import model.TypeFactory;

public class XMLReader extends DefaultHandler {

    private SelectionTable table;
    private String tableName;
    private Boolean tagOpened;
    private Record tempRecord;
    private Class<?>[] types;

    public XMLReader(String tableName) {
        this.tableName = tableName;
        tagOpened = false;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!tagOpened)
            return;
        String content = String.copyValueOf(ch, start, length);
        if (content.equals("null"))
            tempRecord.addToRecord(null);
        else {
            Class<?> tempClz = types[tempRecord.getValues().size()];

            try {
                if (tempClz.equals(String.class))
                    tempRecord.addToRecord(types[tempRecord.getValues().size()]
                            .getMethod("valueOf", Object.class).invoke(null, content));
                else
                    tempRecord.addToRecord(types[tempRecord.getValues().size()]
                            .getMethod("valueOf", String.class).invoke(null, content));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        tagOpened = false;
        if (qName.equals("Record"))
            table.addRecord(tempRecord);
    }

    private Class<?>[] extractTypes(Map<String, Class<?>> column) {
        Class<?>[] types = new Class<?>[column.keySet().size()];
        int i = 0;
        for (Iterator<String> iterator = column.keySet().iterator(); iterator.hasNext();) {
            String type = iterator.next();
            types[i++] = column.get(type);
        }
        return types;
    }

    private Map<String, Class<?>> getColumns(Attributes attributes) {
        Map<String, Class<?>> ret = new LinkedHashMap<>();
        String names = attributes.getValue("colNames");
        String[] namesArr = names.split(",\\s");
        String types = attributes.getValue("types");
        String[] typesArr = types.split(",\\s");
        for (int i = 0; i < namesArr.length; i++) {
            ret.put(namesArr[i], TypeFactory.getClass(typesArr[i]));
        }
        return ret;
    }

    public SelectionTable getTable() {
        return table;
    }

    @Override
    // Triggered when the start of tag is found.
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (qName.equals(tableName)) {
            Map<String, Class<?>> header = getColumns(attributes);
            table = new SelectionTable(tableName, header);
            this.types = this.extractTypes(header);
        } else if (qName.equals("Record")) {
            tempRecord = new Record(table.getHeader(), new ArrayList<>());
        } else if (table.getHeader().containsKey(qName)) {
            tagOpened = true;
        }
    }
}