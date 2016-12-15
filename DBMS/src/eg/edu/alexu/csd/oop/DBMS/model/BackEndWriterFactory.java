package eg.edu.alexu.csd.oop.DBMS.model;

import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.BackEndWriter;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.json.JSONWriter;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.xml.XMLWriter;

public class BackEndWriterFactory {
    private final static String JSON_CASE = "altdb";
    private final static String XML_CASE = "xmldb";

    public static BackEndWriter getBackEndWriter(String writerType) {
        switch (writerType.toLowerCase()) {
        case XML_CASE:
            return new XMLWriter();
        case JSON_CASE:
            return new JSONWriter();
        default:
            return null;
        }
    }
}
