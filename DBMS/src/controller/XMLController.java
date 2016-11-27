package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

public class XMLController {

    private DBMSController dbmsController;
    private XMLOutputFactory fileWriter;
    private XMLStreamWriter xmlStreamWriter;
    private FileOutputStream writer;

    public XMLController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        fileWriter = XMLOutputFactory.newInstance();

    }

    public File initializeXML(String tablePath, String tableName, List<String> colNames,
            List<Class<?>> types) {
        File xmlFile = new File(tablePath + File.separator + ".xml");
        try {
            writer = new FileOutputStream(xmlFile);
            xmlStreamWriter = fileWriter.createXMLStreamWriter(writer, "UTF-8");
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
            xmlStreamWriter.writeCharacters("\n");
            xmlStreamWriter.writeDTD(
                    "<!DOCTYPE " + tableName + " SYSTEM " + "\"" + tableName + ".dtd\">\n");
            xmlStreamWriter.writeStartElement(tableName);
            xmlStreamWriter.writeAttribute("colNames", listToString(colNames));
            xmlStreamWriter.writeAttribute("types", listToString(types));
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
        File dtdFile = new File(tablePath + File.separator + ".dtd");
        try {
            writer = new FileOutputStream(dtdFile);
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("<!ELEMENT " + tableName + " (Record*)>\n");
            strBuilder.append("<!ElEMENT Record (");
            for (int i = 0; i < colNames.size(); i++) {
                strBuilder.append(colNames.get(i));
                if (i != colNames.size() - 1) {
                    strBuilder.append(",");
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
            e.printStackTrace();
        }
        return dtdFile;

    }

    public String listToString(List<?> list) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            strBuilder.append(list.get(i).toString());
            if (i != list.size() - 1)
                strBuilder.append(",");
        }

        return strBuilder.toString();
    }

}
