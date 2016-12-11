package model;

import controller.backEnd.BackEndWriter;
import controller.backEnd.JSONWriter;
import controller.backEnd.XMLWriter;

public class BackEndWriterFactory {
    private final static String XML_CASE = "xml";
    private final static String JSON_CASE = "json";

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
