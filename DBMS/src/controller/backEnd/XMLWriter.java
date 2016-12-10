package controller.backEnd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import model.ClassFactory;
import model.Record;
import model.SelectionTable;
import model.Table;

public class XMLWriter implements BackEndWriter {

    private static final String DATA_FILE_EXTENSION = ".xml";
    private static final String VALIDATOR_FILE_EXTENSION = ".dtd";

    private org.jdom2.Document document;
    private XMLEventReader eventReader;
    private XMLOutputFactory fileWriter;
    private XMLInputFactory inputFactory;
    private SAXBuilder saxBuilder;
    private FileOutputStream writer;
    private XMLOutputter xmlOutput;
    private XMLStreamWriter xmlStreamWriter;

    public XMLWriter() {
        fileWriter = XMLOutputFactory.newInstance();
        saxBuilder = new SAXBuilder();
        xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        inputFactory = XMLInputFactory.newInstance();
    }

    private String classToString(Map<String, Class<?>> map) {
        StringBuilder strBuilder = new StringBuilder();
        int counter = 0;
        for (String name : map.keySet()) {
            strBuilder.append(map.get(name).getSimpleName());
            if (counter != map.size() - 1)
                strBuilder.append(", ");
            counter++;
        }
        return strBuilder.toString();
    }

    private Map<String, Class<?>> getColumnsNames(Table table) {
        File tableXML = table.getData();
        Map<String, Class<?>> ret = new LinkedHashMap<>();
        try {
            eventReader = inputFactory.createXMLEventReader(new FileReader(tableXML));
            document = saxBuilder.build(tableXML);
            Element root = document.getRootElement();
            String names = root.getAttributeValue("colNames");
            String[] namesArr = names.split(",\\s");
            String types = root.getAttributeValue("types");
            String[] typesArr = types.split(",\\s");
            for (int i = 0; i < namesArr.length; i++) {
                ret.put(namesArr[i], ClassFactory.getClass(typesArr[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public String getDataFileExtension() {
        return XMLWriter.DATA_FILE_EXTENSION;
    }

    @Override
    public String getValidatorFileExtension() {
        return XMLWriter.VALIDATOR_FILE_EXTENSION;
    }

    @Override
    public File makeDataFile(String tablePath, String tableName, Map<String, Class<?>> header) {
        File xmlFile = new File(tablePath + File.separator + tableName + ".xml");
        try {
            writer = new FileOutputStream(xmlFile);
            xmlStreamWriter = fileWriter.createXMLStreamWriter(writer, "UTF-8");
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
            xmlStreamWriter.writeCharacters("\n");
            xmlStreamWriter.writeStartElement(tableName);
            xmlStreamWriter.writeAttribute("colNames", mapToString(header));
            xmlStreamWriter.writeAttribute("types", classToString(header));
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlFile;
    }

    @Override
    public File makeValidatorFile(String tablePath, String tableName,
            Map<String, Class<?>> header) {
        File dtdFile = new File(tablePath + File.separator + tableName + ".dtd");
        try {
            writer = new FileOutputStream(dtdFile);
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("<!ELEMENT " + tableName + " (Record*)>\n");
            strBuilder.append("<!ELEMENT Record (");
            int counter = 0;
            for (String name : header.keySet()) {
                strBuilder.append(name);
                if (counter != header.size() - 1) {
                    strBuilder.append(", ");
                }
                counter++;
            }
            strBuilder.append(")>\n");
            for (String name : header.keySet()) {
                strBuilder.append("<!ELEMENT " + name + " (#PCDATA)>\n");
            }
            strBuilder.append("<!ATTLIST " + tableName + " colNames CDATA #REQUIRED>\n");
            strBuilder.append("<!ATTLIST " + tableName + " types CDATA #REQUIRED>\n");
            writer.write(strBuilder.toString().getBytes());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Cannot generate dtd file.");
        }
        return dtdFile;
    }

    private String mapToString(Map<String, Class<?>> map) {

        StringBuilder strBuilder = new StringBuilder();
        int counter = 0;
        for (String name : map.keySet()) {
            strBuilder.append(name);
            if (counter != map.size() - 1)
                strBuilder.append(", ");
            counter++;
        }
        return strBuilder.toString();
    }

    @Override
    public SelectionTable readTable(Table table) throws FileNotFoundException {
        List<Object> values = new ArrayList<>();
        Map<String, Class<?>> names = getColumnsNames(table);
        SelectionTable selectionTable = new SelectionTable(table.getTableName(), names);
        File tableXML = table.getData();
        try {
            eventReader = inputFactory.createXMLEventReader(new FileReader(tableXML));
            document = saxBuilder.build(tableXML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (eventReader.hasNext()) {
            XMLEvent event = null;
            try {
                event = eventReader.nextEvent();
                switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    String startElementName = event.asStartElement().getName().getLocalPart();
                    if (!(startElementName.equals("Record")
                            || startElementName.equals(table.getTableName()))) {
                        Characters chars = null;
                        chars = (Characters) eventReader.nextEvent();
                        if (chars.getData() == "null")
                            values.add(null);
                        else
                            values.add(chars.getData());
                        eventReader.nextEvent();
                    } else {
                        values = new ArrayList<>();
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    String endElementName = event.asEndElement().getName().getLocalPart();
                    if (endElementName.equals("Record")) {
                        Record tempRecord = new Record(names, values);
                        selectionTable.addRecord(tempRecord);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return selectionTable;
    }

    @Override
    public void writeTable(SelectionTable selectionTable) throws FileNotFoundException {
        try {
            File tableXML = selectionTable.getTableSchema().getData();
            eventReader = inputFactory.createXMLEventReader(new FileReader(tableXML));
            writer = new FileOutputStream(tableXML);
            xmlStreamWriter = fileWriter.createXMLStreamWriter(writer, "UTF-8");
            xmlStreamWriter.writeStartElement(selectionTable.getTableName());
            xmlStreamWriter.writeAttribute("colNames", mapToString(selectionTable.getHeader()));
            xmlStreamWriter.writeAttribute("types", classToString(selectionTable.getHeader()));
            xmlStreamWriter.writeEndElement();
            document = saxBuilder.build(tableXML);
            document.getRootElement().removeChildren("Record");
            for (int i = 0; i < selectionTable.getRecordList().size(); i++) {
                Record tempRecord = selectionTable.getRecordList().get(i);
                Element newRecord = new Element("Record");
                int counter = 0;
                for (String name : tempRecord.getColumns().keySet()) {
                    Element tempElement;
                    if (tempRecord.getValues().get(counter) == null)
                        tempElement = new Element(name).setText("null");
                    else
                        tempElement = new Element(name)
                                .setText(tempRecord.getValues().get(counter).toString());
                    newRecord.addContent(tempElement);
                    counter++;
                }
                document.getRootElement().addContent(newRecord);
            }
            xmlOutput.output(document, new FileWriter(tableXML));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot insert into table");
        }
    }
}