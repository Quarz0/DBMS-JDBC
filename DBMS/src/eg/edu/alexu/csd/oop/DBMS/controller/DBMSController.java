package eg.edu.alexu.csd.oop.DBMS.controller;

import java.io.File;

import eg.edu.alexu.csd.oop.DBMS.app.AppLogger;
import eg.edu.alexu.csd.oop.DBMS.controller.backEnd.BackEndWriter;

public class DBMSController {

    private String appPath;
    private DatabaseController databaseController;
    private SQLParserController sqlParserController;

    public DBMSController(String appPath, BackEndWriter backEndWriter) {
        this.databaseController = new DatabaseController(this);
        this.databaseController.getHelper().setBackEndWriter(backEndWriter);
        this.sqlParserController = new SQLParserController(this);
        this.registerObservers();
        this.appPath = appPath;
        this.createAppPath();
    }

    private void createAppPath() {
        File workspace = new File(this.appPath);
        if (!workspace.exists()) {
            if (!workspace.mkdir()) {
                AppLogger.getInstance().fatal("Failed to create DBMS Directory");
                throw new RuntimeException("Failed to create DBMS Directory");
            }
        }
        this.databaseController.getHelper().setWorkspaceDir(workspace);
    }

    public String getAppPath() {
        return appPath;
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
