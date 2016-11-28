package controller;

import view.CLI;

public class CLIController {

    private DBMSController dbmsController;
    private CLI cli;

    public CLIController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        this.cli = new CLI(dbmsController);
    }

    public void begin() {
        this.cli.run();
    }

    public void end() {

    }

    public void newInput(String s) {
        this.dbmsController.getSQLParserController().parse(s);
    }

    public void callForFailure(String errorMessage) {
        this.cli.newPrompt(errorMessage);
    }

    public void draw(String shape) {
        this.cli.newPrompt(shape);
    }

    public void status(Boolean status) {
       
    }

}
