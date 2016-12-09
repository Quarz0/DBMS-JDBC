package controller;

import java.text.ParseException;

import model.Observer;
import util.ErrorCode;
import view.CLI;

public class CLIController implements Observer {

    private CLI cli;
    private DBMSController dbmsController;

    public CLIController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        this.cli = new CLI(dbmsController);
    }

    public void begin() {
        this.cli.run();
    }

    public void callForFailure(String errorMessage) {
        this.cli.newPrompt(errorMessage);
    }

    public void draw(String shape) {
        this.cli.newPrompt(shape);
    }

    public void end() {

    }

    private void newInput(String s) {
        try {
            this.dbmsController.getSQLParserController().parse(s);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void status(Boolean status) {
        if (status)
            this.cli.newPrompt(ErrorCode.QUERY_IS_OK);
        else
            this.cli.newPrompt(ErrorCode.QUERY_IS_NOT_OK);
    }

    @Override
    public void update() {
        this.newInput(this.cli.getInput().trim());
    }

}
