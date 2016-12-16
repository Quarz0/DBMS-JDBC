package eg.edu.alexu.csd.oop.DBMS.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import eg.edu.alexu.csd.oop.DBMS.app.AppLogger;
import eg.edu.alexu.csd.oop.DBMS.plugins.jdbc.Driver;
import eg.edu.alexu.csd.oop.DBMS.util.App;
import eg.edu.alexu.csd.oop.DBMS.util.ErrorCode;
import eg.edu.alexu.csd.oop.DBMS.view.CLI;

public class CLIController implements Feedback {

    public static void exit() {
        AppLogger.getInstance().info("EXITING Program");
        System.exit(0);
    }

    public static void main(String[] args) {
        new CLIController();
    }

    private CLI cli;
    private java.sql.Driver driver;

    private Connection connection;

    private Statement statement;

    public CLIController() {
        this.driver = new Driver();
        this.cli = new CLI(this);
        this.begin();
    }

    public void begin() {
        String url = this.cli.getURL();
        try {
            if (!this.driver.acceptsURL(url)) {
                this.cli.out("Invalid URL!");
                this.end();
            }
        } catch (SQLException e1) {
            this.cli.out("Error!");
            this.end();
        }
        Properties properties = new Properties();
        properties.setProperty("username", System.getProperty("user.name"));
        properties.setProperty("password", this.cli.getPassword());
        properties.setProperty("path", App.DEFAULT_DIR_PATH);
        try {
            this.connection = this.driver.connect(url, properties);
            this.statement = this.connection.createStatement();
        } catch (SQLException e) {
            this.cli.out(e.getMessage());
            this.end();
        }
        this.cli.run();
    }

    public void end() {
        this.cli.close();
        CLIController.exit();
    }

    @Override
    public void feedback(String feedback) {
        this.cli.setTable(feedback);
    }

    public String newInput(String s) {
        try {
            if (this.statement.execute(s))
                return this.statement.getResultSet().toString();
            else
                return ErrorCode.QUERY_IS_OK;
        } catch (RuntimeException e) {
            return e.getMessage();
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

}
