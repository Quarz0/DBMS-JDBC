package eg.edu.alexu.csd.oop.DBMS.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eg.edu.alexu.csd.oop.DBMS.util.App;

public class AppLogger {

    private static Logger logger;

    public static Logger getInstance() {
        if (!App.checkForExistence(logger))
            logger = LogManager.getLogger();
        return logger;
    }

    private AppLogger() {
    }
}
