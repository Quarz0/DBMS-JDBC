package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import controller.DBMSController;
import util.App;
import util.ErrorCode;

public class CLI {

    private BufferedReader bufferedReader;
    private DBMSController dbmsController;
    private String feedback;
    private String table;

    public CLI(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        this.feedback = "";
        this.table = null;
    }

    public void close() {
        try {
            this.bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ErrorCode.CLI_CLOSE + " " + e.getMessage());
        }
    }

    public void newPrompt() {
        try {
            while (true) {
                this.print(this.feedback);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ErrorCode.CLI_READLINE + " " + e.getMessage());
        }
    }

    private void print(String s) throws IOException {
        if (App.checkForExistence(s))
            System.out.println(s);
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
        this.feedback = this.dbmsController.getCLIController()
                .newInput(this.bufferedReader.readLine());
    }

    public void setTable(String table) {
        this.table = table;
    }

}
