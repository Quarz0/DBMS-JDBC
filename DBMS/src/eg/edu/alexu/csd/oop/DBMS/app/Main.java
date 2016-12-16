package eg.edu.alexu.csd.oop.DBMS.app;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMSController;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.xml.XMLWriter;
import eg.edu.alexu.csd.oop.DBMS.util.App;

public class Main {

    public static void exit() {
        AppLogger.getInstance().info("EXITING Program");
        System.exit(0);
    }

    public static void main(String[] args) {
        AppLogger.getInstance().info("Program STARTED");
        new DBMSController(App.DEFAULT_DIR_PATH, new XMLWriter());
    }

}
