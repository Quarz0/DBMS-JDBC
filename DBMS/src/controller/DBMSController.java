package controller;

import java.io.File;

import controller.backEnd.BackEndWriter;

public class DBMSController {

    private String appPath;
    private CLIController cliController;
    private DatabaseController databaseController;
    private SQLParserController sqlParserController;

    public DBMSController(String appPath, BackEndWriter backEndWriter) {
        this.cliController = new CLIController(this);
        this.databaseController = new DatabaseController(this);
        this.databaseController.getHelper().setBackEndWriter(backEndWriter);
        this.sqlParserController = new SQLParserController(this);
        this.registerObservers();
        this.appPath = appPath;
        this.createAppPath();
        // this.cliController.begin();
    }

    private void createAppPath() {
        File workspace = new File(this.appPath);
        if (!workspace.exists()) {
            if (!workspace.mkdir()) {
                throw new RuntimeException("Failed to create DBMS Dirctory");
            }
        }
        this.databaseController.getHelper().setWorkspaceDir(workspace);
    }

    public String getAppPath() {
        return appPath;
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