package model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class LogInConfig {
    Properties confProps;

    public LogInConfig(String DBMSDir) throws IOException {
        confProps = new Properties();
        FileReader reader = new FileReader(DBMSDir + File.separator + "login.properties");
        confProps.load(reader);
    }

    /**
     * Retrieves the value of a key in the configuration file.
     * 
     * @param key
     *            the attribute to get its value.
     * @return the value of the key if not found returns null.
     */
    public String getProperty(String key) {
        return confProps.getProperty(key);
    }
}
