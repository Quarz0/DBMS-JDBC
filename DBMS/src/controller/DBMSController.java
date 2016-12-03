package controller;

import java.io.File;

import app.Main;
import util.App;
import util.ErrorCode;

public class DBMSController {

    private CLIController cliController;
    private DatabaseController databaseController;
    private SQLParserController sqlParserController;
    private XMLController xmlController;

    public DBMSController() {
        this.cliController = new CLIController(this);
        this.databaseController = new DatabaseController(this);
        this.sqlParserController = new SQLParserController(this);
        this.xmlController = new XMLController(this);
        this.registerObservers();
        this.createAppPath();
        this.cliController.begin();
    }

    private void createAppPath() {
        File workspace = new File(App.DEFAULT_DIR_PATH);
        if (!workspace.exists()) {
            if (!workspace.mkdir()) {
                this.cliController.callForFailure(ErrorCode.FAILED_TO_CREATE_DEFAULT_DATABASE);
            }
        }
        this.databaseController.getHelper().setWorkspaceDir(workspace);
    }

    public void exit() {
        this.cliController.end();
        Main.exit();
    }

    public CLIController getCLIController() {
        return this.cliController;
    }

    public DatabaseController getDatabaseController() {
        return this.databaseController;
    }

    public SQLParserController getSQLParserController() {
        return this.sqlParserController;
    }

    public XMLController getXMLController() {
        return this.xmlController;
    }

    private void registerObservers() {
        this.sqlParserController.getSqlParserHelper().registerObserver(this.databaseController);
    }
}
