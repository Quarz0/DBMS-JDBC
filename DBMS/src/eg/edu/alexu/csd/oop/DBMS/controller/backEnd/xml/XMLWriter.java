package eg.edu.alexu.csd.oop.DBMS.controller.backEnd.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.BackEndWriter;
import eg.edu.alexu.csd.oop.DBMS.model.Record;
import eg.edu.alexu.csd.oop.DBMS.model.SelectionTable;
import eg.edu.alexu.csd.oop.DBMS.model.Table;

public class XMLWriter implements BackEndWriter {
    private static final String DATA_FILE_EXTENSION = ".xml";
    private static final String VALIDATOR_FILE_EXTENSION = ".dtd";
    private XMLOutputFactory fileWriter;
    private SAXParser parser;
    private XMLReader reader;
    private FileOutputStream writer;
    private XMLStreamWriter xmlStreamWriter;

    public XMLWriter() {
        SAXParserFactory parserFactor = SAXParserFactory.newInstance();
        try {
            parser = parserFactor.newSAXParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileWriter = XMLOutputFactory.newInstance();
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

    @Override
    public String getDataFileExtension() {
        return XMLWriter.DATA_FILE_EXTENSION;
    }

    @Override
    public String getValidatorFileExtension() {
        return XMLWriter.VALIDATOR_FILE_EXTENSION;
    }

    @Override
    public File makeDataFile(String tablePath, String tableName, Map<String, Class<?>> header)
            throws IOException, XMLStreamException {
        File xmlFile = new File(tablePath + File.separator + tableName + ".xml");
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
    public SelectionTable readTable(Table table) {
        reader = new XMLReader(table.getTableName());
        try {
            parser.parse(table.getData(), reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid file");
        }
        return reader.getTable();
    }

    @Override
    public void writeTable(SelectionTable selectionTable) throws FileNotFoundException {
        try {
            File tableXML = selectionTable.getTableSchema().getData();
            writer = new FileOutputStream(tableXML);
            xmlStreamWriter = fileWriter.createXMLStreamWriter(writer, "UTF-8");
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
            xmlStreamWriter.writeCharacters("\n");
            xmlStreamWriter.writeStartElement(selectionTable.getTableName());
            xmlStreamWriter.writeAttribute("colNames", mapToString(selectionTable.getHeader()));
            xmlStreamWriter.writeAttribute("types", classToString(selectionTable.getHeader()));
            for (int i = 0; i < selectionTable.getRecordList().size(); i++) {
                xmlStreamWriter.writeCharacters("\n\t");
                xmlStreamWriter.writeStartElement("Record");
                Record tempRecord = selectionTable.getRecordList().get(i);
                int counter = 0;
                for (String name : tempRecord.getColumns().keySet()) {
                    Object value = tempRecord.getValues().get(counter);
                    xmlStreamWriter.writeCharacters("\n\t\t");
                    if (value == null) {
                        xmlStreamWriter.writeEmptyElement(name);
                        xmlStreamWriter.writeAttribute("xsi:nil", "true");
                    } else {
                        xmlStreamWriter.writeStartElement(name);
                        xmlStreamWriter.writeCharacters(value.toString());
                        xmlStreamWriter.writeEndElement();
                    }
                    counter++;
                }
                xmlStreamWriter.writeCharacters("\n\t");
                xmlStreamWriter.writeEndElement();
            }
            xmlStreamWriter.writeCharacters("\n");
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeCharacters("\n");
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot insert into table");
        }
    }

}