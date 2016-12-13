package app;

import controller.DBMSController;
import controller.backEnd.xml.XMLWriter;
import util.App;

public class Main {

    public static void exit() {
        System.exit(0);
    }

    public static void main(String[] args) {
        new DBMSController(App.DEFAULT_DIR_PATH, new XMLWriter());
    }

}
