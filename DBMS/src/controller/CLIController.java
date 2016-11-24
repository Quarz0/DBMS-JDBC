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
	}

}
