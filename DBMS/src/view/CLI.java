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
    private String input;

    public CLI(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        this.input = null;
    }

    public void close() {
        try {
            this.bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ErrorCode.CLI_CLOSE + " " + e.getMessage());
        }
    }

    public String getInput() {
        return input;
    }

    public void newPrompt(String s) {
        try {
            while (true) {
                this.print(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ErrorCode.CLI_READLINE + " " + e.getMessage());
        }
    }

    private void print(String s) throws IOException {
        if (App.checkForExistence(s))
            System.out.println(s);
        System.out.print(App.PS1);
        this.scan();
    }

    public void run() {
        this.newPrompt("");
    }

    private void scan() throws IOException {
        this.input = this.bufferedReader.readLine();
        this.dbmsController.getCLIController().update();
    }

}
