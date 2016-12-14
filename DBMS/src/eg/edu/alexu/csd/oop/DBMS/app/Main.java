package eg.edu.alexu.csd.oop.DBMS.app;

import java.util.ArrayList;
import java.util.List;

import eg.edu.alexu.csd.TestRunner;
import eg.edu.alexu.csd.oop.DBMS.controller.DBMSController;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.xml.XMLWriter;
import eg.edu.alexu.csd.oop.DBMS.util.App;

public class Main {

    public static void exit() {
        System.exit(0);
    }

    public static void main(String[] args) {
        new DBMSController(App.DEFAULT_DIR_PATH, new XMLWriter());
    }

}
