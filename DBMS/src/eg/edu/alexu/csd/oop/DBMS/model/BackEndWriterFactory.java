package eg.edu.alexu.csd.oop.DBMS.model;

import java.util.Random;

import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.BackEndWriter;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.json.JSONWriter;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.protoBuf.ProtoBufWriter;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.xml.XMLWriter;

public class BackEndWriterFactory {
    private final static String JSON_CASE = "json";
    private final static String XML_CASE = "xml";
    private final static String PROTO_CASE = "proto";
    private final static String ALTERNATIVE_CASE = "alt";

    public static BackEndWriter getBackEndWriter(String writerType) {
        switch (writerType.toLowerCase()) {
        case XML_CASE:
            return new XMLWriter();
        case JSON_CASE:
            return new JSONWriter();
        case PROTO_CASE:
            return new ProtoBufWriter();
        case ALTERNATIVE_CASE:
            return getRandomWriter();
        default:
            return null;
        }
    }

    private static BackEndWriter getRandomWriter() {
        Random random = new Random();
        int i = random.nextInt(3) + 1;
        switch (i) {
        case 1:
            return new XMLWriter();
        case 2:
            return new JSONWriter();
        case 3:
            return new ProtoBufWriter();
        default:
            return null;
        }
    }
}
