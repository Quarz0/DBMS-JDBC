package eg.edu.alexu.csd.oop.DBMS.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import eg.edu.alexu.csd.oop.DBMS.app.AppLogger;
import eg.edu.alexu.csd.oop.DBMS.controller.CLIController;
import eg.edu.alexu.csd.oop.DBMS.util.App;
import eg.edu.alexu.csd.oop.DBMS.util.ErrorCode;

public class CLI {

    private BufferedReader bufferedReader;
    private CLIController cliController;
    private String feedback;
    private long start;
    private String table;

    public CLI(CLIController cliController) {
        this.cliController = cliController;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        this.feedback = "";
        this.table = null;
    }

    public void close() {
        try {
            this.bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            AppLogger.getInstance().error(ErrorCode.CLI_CLOSE + " " + e.getMessage());
        }
    }

    public String getPassword() {
        System.out.print("Enter password: ");
        // char[] pass = System.console().readPassword();
        // return new String(pass);
        try {
            return this.bufferedReader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public String getURL() {
        System.out.print("Enter Driver's URL: ");
        try {
            return this.bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void newPrompt() {
        try {
            while (true) {
                this.print();
            }
        } catch (IOException e) {
            e.printStackTrace();
            AppLogger.getInstance().error(ErrorCode.CLI_READLINE + " " + e.getMessage());
        }
    }

    public void out(String s) {
        System.out.println(s);
    }

    private void print() throws IOException {
        if (App.checkForExistence(this.feedback)) {
            String log = this.feedback + " (" + (System.currentTimeMillis() - this.start) + "ms)";
            System.out.println(log);
            AppLogger.getInstance().info(log);
        }
        if (App.checkForExistence(this.table))
            System.out.println(this.table);
        this.table = null;
        System.out.print(App.PS1);
        this.scan();
    }

    public void run() {
        this.newPrompt();
    }

    private void scan() throws IOException {
        String temp = this.bufferedReader.readLine();
        AppLogger.getInstance().info("USER INPUT: <" + temp + ">");
        this.start = System.currentTimeMillis();
        this.feedback = this.cliController.newInput(temp);
    }

    public void setTable(String table) {
        this.table = table;
    }

}
