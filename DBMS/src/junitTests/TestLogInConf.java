package junitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

import controller.DBMSController;
import controller.LoginController;
import controller.backEnd.xml.XMLWriter;
import util.App;

public class TestLogInConf {

    @Test
    public void test() {
        LoginController logger = null;
        try {
            logger = new LoginController(new DBMSController(App.DEFAULT_DIR_PATH, new XMLWriter()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(logger.canLogIn("Mahmoud", "Hussein"));
    }
}
