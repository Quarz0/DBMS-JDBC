package eg.edu.alexu.csd.oop.DBMS.controller;

import java.text.ParseException;

import eg.edu.alexu.csd.oop.DBMS.util.ErrorCode;
import eg.edu.alexu.csd.oop.DBMS.view.CLI;

public class CLIController implements Feedback {

    private CLI cli;
    private DBMSController dbmsController;

    public CLIController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        this.cli = new CLI(dbmsController);
    }

    public void begin() {
        this.cli.run();
    }

    public void end() {

    }

    @Override
    public void feedback(String feedback) {
        this.cli.setTable(feedback);
    }

    public String newInput(String s) {
        try {
            this.dbmsController.getSQLParserController().parse(s);
            return ErrorCode.QUERY_IS_OK;
        } catch (ParseException e) {
            return e.getMessage();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

}
