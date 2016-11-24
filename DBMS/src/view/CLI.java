package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import controller.DBMSController;
import util.App;
import util.ErrorCode;

public class CLI {

	private DBMSController dbmsController;
	private BufferedReader bufferedReader;

	public CLI(DBMSController dbmsController) {
		this.dbmsController = dbmsController;
		this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}

	public void run() {
		this.newPrompt("");
	}

	public void close() {
		try {
			this.bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(ErrorCode.CLI_CLOSE + " " + e.getMessage());
		}
	}

	private void print(String s) throws IOException {
		System.out.print(App.PS1 + s);
		this.scan();
	}

	private void scan() throws IOException {
		this.dbmsController.getCLIController().newInput(this.bufferedReader.readLine());
	}

	public void newPrompt(String s) {
		try {
			this.print(s);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(ErrorCode.CLI_READLINE + " " + e.getMessage());
		}
	}

}
