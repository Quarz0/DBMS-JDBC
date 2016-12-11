package model;

import controller.backEnd.BackEndWriter;
import controller.backEnd.json.JSONWriter;
import controller.backEnd.xml.XMLWriter;

public class BackEndWriterFactory {
    private final static String JSON_CASE = "json";
    private final static String XML_CASE = "xml";

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
