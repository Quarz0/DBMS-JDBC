package model;

import controller.backEnd.BackEndWriter;
import controller.backEnd.XMLWriter;

public class BackEndWriterFactory {
    private final static String XML_CASE = "xml";

    public static BackEndWriter getBackEndWriter(String writerType) {
        switch (writerType.toLowerCase()) {
        case XML_CASE:
            return new XMLWriter();
        default:
            return null;
        }
    }
}
