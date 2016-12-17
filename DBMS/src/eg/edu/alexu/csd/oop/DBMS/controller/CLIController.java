package eg.edu.alexu.csd.oop.DBMS.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import eg.edu.alexu.csd.oop.DBMS.app.AppLogger;
import eg.edu.alexu.csd.oop.DBMS.plugins.jdbc.Driver;
import eg.edu.alexu.csd.oop.DBMS.util.App;
import eg.edu.alexu.csd.oop.DBMS.util.ErrorCode;
import eg.edu.alexu.csd.oop.DBMS.util.MessageDigestUtil;
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
    private Driver driver;

    private Connection connection;

    private Statement statement;

    public CLIController() {
        this.driver = new Driver();
        this.cli = new CLI(this);
        this.begin();
    }

    public void begin() {
        try {
            this.checkForCredentials();
        } catch (NoSuchAlgorithmException | RuntimeException | IOException e2) {
            this.cli.out(e2.getMessage());
            this.end();
        }
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
        this.cli.getInfo().setProperty("username", System.getProperty("user.name"));
        this.cli.getInfo().setProperty("password", this.cli.getPassword());
        try {
            this.connection = this.driver.connect(url, this.cli.getInfo());
            if (!App.checkForExistence(this.connection))
                throw new SQLException("Access denied for user "
                        + this.cli.getInfo().getProperty("username") + "@" + url);
            this.statement = this.connection.createStatement();
        } catch (SQLException e) {
            this.cli.out(e.getMessage());
            this.end();
        }
        this.cli.run();
    }

    public void checkForCredentials()
            throws NoSuchAlgorithmException, IOException, RuntimeException {
        if (this.cli.credentialsExists())
            return;
        this.cli.newCredentials(System.getProperty("user.name"),
                MessageDigestUtil.getSecuredPassword(this.cli.newPassword()));
    }

    public void end() {
        try {
            this.statement.close();
            this.connection.close();
            Thread.sleep(2000);
            this.cli.out("Bye");
            Thread.sleep(2000);
            this.cli.close();
            CLIController.exit();
        } catch (InterruptedException e) {
            this.cli.out("Error!");
        } catch (SQLException e) {
            this.cli.out("Error while trying to close connection");
        }
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
                return ErrorCode.QUERY_IS_OK + " " + (this.statement.getUpdateCount() + " row"
                        + (this.statement.getUpdateCount() == 1 ? " " : "s ") + "affected");
        } catch (RuntimeException e) {
            return e.getMessage();
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

}
