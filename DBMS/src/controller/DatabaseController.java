package controller;

public class DatabaseController implements DBMS {

	private DBMSController dbmsController;

	public DatabaseController(DBMSController dbmsController) {
		this.dbmsController = dbmsController;
	}
}
