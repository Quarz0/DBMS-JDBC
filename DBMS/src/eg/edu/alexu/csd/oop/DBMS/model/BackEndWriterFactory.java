package eg.edu.alexu.csd.oop.DBMS.model;

import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.BackEndWriter;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.protoBuf.ProtoBufWriter;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.xml.XMLWriter;

public class BackEndWriterFactory {
    private final static String JSON_CASE = "json";
    private final static String XML_CASE = "xml";
    private final static String ALTERNATIVE_CASE = "alt";

    public static BackEndWriter getBackEndWriter(String writerType) {
        switch (writerType.toLowerCase()) {
        case XML_CASE:
            return new XMLWriter();
        case JSON_CASE:
        case ALTERNATIVE_CASE:
            return new ProtoBufWriter();
        default:
            return null;
        }
    }
}
