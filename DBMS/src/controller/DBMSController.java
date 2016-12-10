package controller;

import java.io.File;

import app.Main;
import util.App;

public class DBMSController {

    private CLIController cliController;
    private DatabaseController databaseController;
    private SQLParserController sqlParserController;

    public DBMSController() {
        this.cliController = new CLIController(this);
        this.databaseController = new DatabaseController(this);
        this.sqlParserController = new SQLParserController(this);
        this.registerObservers();
        this.createAppPath();
        this.cliController.begin();
    }

    private void createAppPath() {
        File workspace = new File(App.DEFAULT_DIR_PATH);
        if (!workspace.exists()) {
            if (!workspace.mkdir()) {
                throw new RuntimeException("Failed to create DBMS Dirctory");
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

    private void registerObservers() {
        this.sqlParserController.getSqlParserHelper().registerObserver(this.databaseController);
    }
}
