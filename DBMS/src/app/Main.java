package app;

import controller.DBMSController;

public class Main {

    public static void exit() {
        System.exit(0);
    }

    public static void main(String[] args) {
        new DBMSController();
    }

}
