package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import model.ClassFactory;
import model.Record;
import model.SelectionTable;
import model.Table;

public class XMLController implements BackEndWriter {

    public static boolean validateWithDTDUsingSAX(File xmlFile)
            throws ParserConfigurationException, IOException {
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);

            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();

            reader.setErrorHandler(new ErrorHandler() {
                @Override
                public void error(SAXParseException e) throws SAXException {
                    System.out.println("ERROR : " + e.getMessage());
                    throw e;
                }

                @Override
                public void fatalError(SAXParseException e) throws SAXException {
                    System.out.println("FATAL : " + e.getMessage());
                    throw e;
                }

                @Override
                public void warning(SAXParseException e) throws SAXException {
                    System.out.println("WARNING : " + e.getMessage()); // do nothing
                }
            });
            reader.parse(new InputSource(new FileReader(xmlFile)));
            return true;
        } catch (ParserConfigurationException pce) {
            throw pce;
        } catch (IOException io) {
            throw io;
        } catch (SAXException se) {
            return false;
        }
    }

    private DBMSController dbmsController;
    private org.jdom2.Document document;
    private XMLEventReader eventReader;
    private XMLOutputFactory fileWriter;
    private XMLInputFactory inputFactory;
    private SAXBuilder saxBuilder;
    private FileOutputStream writer;
    private XMLOutputter xmlOutput;

    private XMLStreamWriter xmlStreamWriter;

    public XMLController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        fileWriter = XMLOutputFactory.newInstance();
        saxBuilder = new SAXBuilder();
        xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        inputFactory = XMLInputFactory.newInstance();

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

    public Map<String, Class<?>> getColumns(Table table) {
        File tableXML = table.getXML();
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

    public File initializeDTD(String tablePath, String tableName, List<String> colNames,
            List<Class<?>> types) {
        File dtdFile = new File(tablePath + File.separator + tableName + ".dtd");
        try {
            writer = new FileOutputStream(dtdFile);
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("<!ELEMENT " + tableName + " (Record*)>\n");
            strBuilder.append("<!ELEMENT Record (");
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

    public File initializeXML(String tablePath, String tableName, List<String> colNames,
            List<Class<?>> types) {
        File xmlFile = new File(tablePath + File.separator + tableName + ".xml");
        try {
            writer = new FileOutputStream(xmlFile);
            xmlStreamWriter = fileWriter.createXMLStreamWriter(writer, "UTF-8");
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
            xmlStreamWriter.writeCharacters("\n");
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

    private String listToString(List<?> list) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            strBuilder.append(list.get(i).toString());
            if (i != list.size() - 1)
                strBuilder.append(", ");
        }
        return strBuilder.toString();
    }

    @Override
    public SelectionTable readTable(Table table) throws FileNotFoundException {
        List<Object> values = new ArrayList<>();
        Map<String, Class<?>> names = getColumnsNames(table);
        SelectionTable selectionTable = new SelectionTable(table.getTableName(), names);
        File tableXML = table.getXML();
        try {
            eventReader = inputFactory.createXMLEventReader(new FileReader(tableXML));
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            document = saxBuilder.build(tableXML);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(document.getRootElement().getName());
        while (eventReader.hasNext()) {
            XMLEvent event = null;
            try {
                event = eventReader.nextEvent();
            } catch (XMLStreamException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            switch (event.getEventType()) {
            case XMLStreamConstants.START_ELEMENT:
                String startElementName = event.asStartElement().getName().getLocalPart();
                if (!(startElementName.equals("Record")
                        || startElementName.equals(table.getTableName()))) {
                    Characters chars = null;
                    try {
                        chars = (Characters) eventReader.nextEvent();
                    } catch (XMLStreamException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (chars.getData() == "null")
                        values.add(null);
                    else
                        values.add(chars.getData());
                    try {
                        eventReader.nextEvent();
                    } catch (XMLStreamException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
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
        }
        return selectionTable;
    }

}