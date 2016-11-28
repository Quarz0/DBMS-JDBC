package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import model.Record;
import model.SelectionTable;
import model.Table;

public class XMLController {

    private DBMSController dbmsController;
    private XMLOutputFactory fileWriter;
    private XMLInputFactory inputFactory;
    private XMLStreamWriter xmlStreamWriter;
    private XMLEventReader eventReader;
    private FileOutputStream writer;
    private SAXBuilder saxBuilder;
    private org.jdom2.Document document;
    private XMLOutputter xmlOutput;

    public XMLController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        fileWriter = XMLOutputFactory.newInstance();
        saxBuilder = new SAXBuilder();
        xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        inputFactory = XMLInputFactory.newInstance();

    }

    public File initializeXML(String tablePath, String tableName, List<String> colNames,
            List<Class<?>> types) {
        File xmlFile = new File(tablePath + File.separator + tableName + ".xml");
        try {
            writer = new FileOutputStream(xmlFile);
            xmlStreamWriter = fileWriter.createXMLStreamWriter(writer, "UTF-8");
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
            xmlStreamWriter.writeCharacters("\n");
            // xmlStreamWriter.writeDTD(
            // "<!DOCTYPE " + tableName + " SYSTEM " + "\"" + tableName + ".dtd\">\n");
            xmlStreamWriter.writeStartElement(tableName);
            xmlStreamWriter.writeAttribute("colNames", listToString(colNames));
            xmlStreamWriter.writeAttribute("types", classToString(types));
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlFile;

    }

    public File initializeDTD(String tablePath, String tableName, List<String> colNames,
            List<Class<?>> types) {
        File dtdFile = new File(tablePath + File.separator + tableName + ".dtd");
        try {
            writer = new FileOutputStream(dtdFile);
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("<!ELEMENT " + tableName + " (Record*)>\n");
            strBuilder.append("<!ElEMENT Record (");
            for (int i = 0; i < colNames.size(); i++) {
                strBuilder.append(colNames.get(i));
                if (i != colNames.size() - 1) {
                    strBuilder.append(", ");
                }
            }
            strBuilder.append(")>\n");
            for (int i = 0; i < colNames.size(); i++) {
                strBuilder.append("<!ELEMENT " + colNames.get(i) + " (#PCDATA)>\n");
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

    public boolean insertIntoTable(Table table, Record record) {
        try {
            File tableXML = table.getXML();
            List<String> names = getColumnsNames(table);
            Element newRecord = new Element("Record");
            eventReader = inputFactory.createXMLEventReader(new FileReader(tableXML));
            for (int i = 0; i < record.getValues().size(); i++) {
                Element temp;
                if (record.getValues().get(i) == null)
                    temp = new Element(names.get(i)).setText("null");
                else
                    temp = new Element(names.get(i)).setText(record.getValues().get(i).toString());
                newRecord.addContent(temp);
            }
            document = saxBuilder.build(tableXML);
            document.getRootElement().addContent(newRecord);
            xmlOutput.output(document, new FileWriter(tableXML));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot insert into table");
        }
        return true;
    }

    public SelectionTable selectFromTable(Table table, List<String> colNames, String condition) {
        SelectionTable selectionTable = new SelectionTable(table.getTableName(), colNames);
        try {
            File tableXML = table.getXML();
            eventReader = inputFactory.createXMLEventReader(new FileReader(tableXML));
            document = saxBuilder.build(tableXML);
            Object[] values = new Object[colNames.size()];
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    String startElementName = event.asStartElement().getName().getLocalPart();
                    if (!startElementName.equals("Record")) {
                        if (colNames.contains(startElementName)) {
                            Characters chars = (Characters) eventReader.nextEvent();
                            values[colNames.indexOf(startElementName)] = chars.getData();
                            eventReader.nextEvent();
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    String endElementName = event.asEndElement().getName().getLocalPart();
                    if (endElementName.equals("Record")) {
                        Record tempRecord = new Record(colNames,
                                Arrays.asList(Arrays.copyOf(values, values.length)));
                        if (dbmsController.getDatabaseController().evaluate(condition,
                                tempRecord)) {
                            selectionTable.addRecord(tempRecord);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(selectionTable.getHeader());
        for (int i = 0; i < selectionTable.getRecordList().size(); i++) {
            for (int j = 0; j < selectionTable.getRecordList().get(i).getValues().size(); j++) {
                System.out.print(selectionTable.getRecordList().get(i).getValues().get(j) + " ");
            }
            System.out.println();
        }
        return selectionTable;
    }

    public List<String> getColumnsNames(Table table) {
        File dtdFile = table.getDTD();
        List<String> colNames = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(dtdFile));
            reader.readLine();
            String line = reader.readLine();
            line = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
            String[] arr = line.split(",\\s");
            colNames = Arrays.asList(arr);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return colNames;
    }

    private String classToString(List<Class<?>> list) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            strBuilder.append(list.get(i).getSimpleName());
            if (i != list.size() - 1)
                strBuilder.append(", ");
        }
        return strBuilder.toString();
    }

    private String listToString(List<?> list) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            strBuilder.append(list.get(i).toString());
            if (i != list.size() - 1)
                strBuilder.append(", ");
        }
        return strBuilder.toString();
    }

    public List<String> getTypes(Table table) {
        File tableXML = table.getXML();
        List<String> types = new ArrayList<>();
        try {
            eventReader = inputFactory.createXMLEventReader(new FileReader(tableXML));
            document = saxBuilder.build(tableXML);
            Element root = document.getRootElement();
            String typesStr = root.getAttributeValue("types");
            String[] arr = typesStr.split(",\\s");
            types = Arrays.asList(arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return types;
    }
}