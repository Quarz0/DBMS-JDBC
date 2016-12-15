package eg.edu.alexu.csd.oop.DBMS.app;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMSController;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.xml.XMLWriter;

public class Main {

    public static void exit() {
        System.exit(0);
    }

    public static void main(String[] args) {
        String s = "/home/heshamelsawaf/DBMS";
        new DBMSController(s, new XMLWriter());
    }

}
