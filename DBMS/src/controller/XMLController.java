package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.jdom2.DocType;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import model.Record;
import model.Table;

public class XMLController {

    private DBMSController dbmsController;
    private XMLOutputFactory fileWriter;
    private XMLStreamWriter xmlStreamWriter;
    private FileOutputStream writer;
    private SAXBuilder saxBuilder;
    private org.jdom2.Document document;
    private DocType docType;
    private XMLOutputter xmlOutput;

    public XMLController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        fileWriter = XMLOutputFactory.newInstance();
        saxBuilder = new SAXBuilder();
        xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());

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
            System.out.println(tableXML);
            List<String> names = getColumnNames(table);
            document = saxBuilder.build(tableXML);
            docType = document.getDocType();
            Element newRecord = new Element("Record");
            for (int i = 0; i < record.getValues().size(); i++) {
                newRecord.addContent(
                        new Element(names.get(i)).setText(record.getValues().get(i).toString()));
            }
            document.getRootElement().addContent(newRecord);
            xmlOutput.output(document, new FileWriter(tableXML));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot insert into table");
        }
        return true;
    }

    public List<String> getColumnNames(Table table) {
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
}
