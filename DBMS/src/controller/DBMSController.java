package controller;

import app.Main;

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

		this.cliController.begin();
	}

	public void exit() {
		this.cliController.end();
		Main.exit();
	}

	public CLIController getCliController() {
		return this.cliController;
	}

	public DatabaseController getDatabaseController() {
		return this.databaseController;
	}

	public SQLParserController getSqlParserController() {
		return this.sqlParserController;
	}

	public XMLController getXmlController() {
		return this.xmlController;
	}
}
