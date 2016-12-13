package controller;

import java.io.IOException;

import model.LogInConfig;
import util.App;

public class LoginController {
    DBMSController dbmsController;
    LogInConfig logInConfig;

    public LoginController(DBMSController dbmsController) throws IOException {
        this.dbmsController = dbmsController;
        logInConfig = new LogInConfig(dbmsController.getAppPath());
    }

    public boolean canLogIn(String userName, String password) {
        String savedUsername = logInConfig.getProperty("username");
        String savedPassword = logInConfig.getProperty("password");
        if (!App.checkForExistence(savedPassword))
            return false;
        if (!App.checkForExistence(savedUsername))
            return false;
        if (savedUsername.equals(userName) && savedPassword.equals(password)) {
            return true;
        }
        return false;
    }
}
