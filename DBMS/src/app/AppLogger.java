package app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import util.App;

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
