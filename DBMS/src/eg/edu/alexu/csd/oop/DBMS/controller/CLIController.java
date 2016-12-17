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
        System.exit(0);
    }

    public static void main(String[] args) {
        AppLogger.getInstance().info("Program Started");
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
            AppLogger.getInstance().fatal(e2.getMessage());
            this.cli.out(e2.getMessage());
            this.end();
        }
        String url = this.cli.getURL();
        try {
            if (!this.driver.acceptsURL(url)) {
                AppLogger.getInstance().info("Invalid URL!");
                this.cli.out("Invalid URL!");
                this.end();
            }
        } catch (SQLException e1) {
            AppLogger.getInstance().error("Error!");
            this.cli.out("Error!");
            this.end();
        }
        this.cli.getInfo().setProperty("username", System.getProperty("user.name"));
        this.cli.getInfo().setProperty("password", this.cli.getPassword());
        AppLogger.getInstance().info("Password entered");
        try {
            this.connection = this.driver.connect(url, this.cli.getInfo());
            if (!App.checkForExistence(this.connection)) {
                AppLogger.getInstance().error("Access denied for user "
                        + this.cli.getInfo().getProperty("username") + "@" + url);
                throw new SQLException("Access denied for user "
                        + this.cli.getInfo().getProperty("username") + "@" + url);
            }
            this.statement = this.connection.createStatement();
        } catch (SQLException e) {
            AppLogger.getInstance().error(e.getMessage());
            this.cli.out(e.getMessage());
            this.end();
        }
        AppLogger.getInstance().info("Access Granted");
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
            AppLogger.getInstance().warn("Exiting...");
            this.statement.close();
            this.connection.close();
            Thread.sleep(2000);
            this.cli.out("Bye");
            Thread.sleep(2000);
            this.cli.close();
        } catch (InterruptedException e) {
            AppLogger.getInstance().fatal("Error while exiting");
            this.cli.out("Error!");
        } catch (SQLException e) {
            AppLogger.getInstance().fatal("Error while trying to close connection");
            this.cli.out("Error while trying to close connection");
        } catch (Exception e) {
            AppLogger.getInstance().fatal("Error while exiting");
        } finally {
            CLIController.exit();
        }
    }

    @Override
    public void feedback(String feedback) {
        this.cli.setTable(feedback);
    }

    public String newInput(String s) {
        try {
            if (this.statement.execute(s)) {
                AppLogger.getInstance().info(ErrorCode.QUERY_IS_OK);
                return ErrorCode.QUERY_IS_OK + this.statement.getResultSet().toString();
            } else {
                AppLogger.getInstance().info(ErrorCode.QUERY_IS_OK);
                return ErrorCode.QUERY_IS_OK + ", " + (this.statement.getUpdateCount() + " row"
                        + (this.statement.getUpdateCount() == 1 ? " " : "s ") + "affected");
            }
        } catch (SQLException | RuntimeException e) {
            if (App.checkForExistence(e.getMessage()))
                AppLogger.getInstance().error(e.getMessage());
            else
                AppLogger.getInstance().error("Error: " + e);
            return e.getMessage();
        }
    }

}