package eg.edu.alexu.csd.oop.DBMS.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eg.edu.alexu.csd.oop.DBMS.util.App;

public class AppLogger {

    private static Logger logger;

    public static Logger getInstance() {
        if (!App.checkForExistence(logger)) {
            logger = LogManager.getLogger(AppLogger.class.getName());
        }
        return logger;
    }

    private AppLogger() {
    }
    
//    public static void main(String[] args) {
//        AppLogger.getInstance().info("heldsfdlo");
//    }
}
