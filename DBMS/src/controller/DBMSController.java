package controller;

import java.io.File;

import controller.backEnd.BackEndWriter;

public class DBMSController {

    private CLIController cliController;
    private DatabaseController databaseController;
    private SQLParserController sqlParserController;

    public DBMSController(String appDir, BackEndWriter backEndWriter) {
        this.cliController = new CLIController(this);
        this.databaseController = new DatabaseController(this);
        this.databaseController.getHelper().setBackEndWriter(backEndWriter);
        this.sqlParserController = new SQLParserController(this);
        this.registerObservers();
        this.createAppPath(appDir);
        // this.cliController.begin();
    }

    private void createAppPath(String appDir) {
        File workspace = new File(appDir);
        if (!workspace.exists()) {
            if (!workspace.mkdir()) {
                throw new RuntimeException("Failed to create DBMS Dirctory");
            }
        }
        this.databaseController.getHelper().setWorkspaceDir(workspace);
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
