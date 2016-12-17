package eg.edu.alexu.csd.oop.DBMS.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import eg.edu.alexu.csd.oop.DBMS.app.AppLogger;
import eg.edu.alexu.csd.oop.DBMS.controller.CLIController;
import eg.edu.alexu.csd.oop.DBMS.util.App;
import eg.edu.alexu.csd.oop.DBMS.util.ErrorCode;

public class CLI {

    private BufferedReader bufferedReader;
    private CLIController cliController;
    private String feedback;
    private long start;
    private String table;
    private Properties info;

    private File configFile;

    public CLI(CLIController cliController) {
        this.cliController = cliController;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        this.feedback = "";
        this.table = null;
        this.info = new Properties();
        this.info.setProperty("path", App.DEFAULT_DIR_PATH);
        this.createAppPath();
    }

    public void close() {
        try {
            this.bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            AppLogger.getInstance().error(ErrorCode.CLI_CLOSE + " " + e.getMessage());
        }
    }

    private void createAppPath() {
        File workspace = new File(App.DEFAULT_DIR_PATH);
        if (!workspace.exists()) {
            if (!workspace.mkdir()) {
                AppLogger.getInstance().fatal("Failed to create DBMS Directory");
                throw new RuntimeException("Failed to create DBMS Directory");
            }
        }
    }

    public boolean credentialsExists() {
        String configDir = info.getProperty("path");
        this.configFile = new File(configDir + File.separator + ".cred.conf");
        return configFile.exists();
    }

    public Properties getInfo() {
        return info;
    }

    public String getPassword() {
        System.out.print("Enter password: ");
        char[] pass = System.console().readPassword();
        return new String(pass);
        // try {
        // return this.bufferedReader.readLine();
        // } catch (IOException e) {
        // return null;
        // }
    }

    public String getURL() {
        System.out.print("Enter Driver's URL: ");
        try {
            return this.bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void newCredentials(String username, String password) throws IOException {
        String configDir = info.getProperty("path");
        List<String> lines = Arrays.asList("username:" + username, "password:" + password);
        File file = new File(configDir + File.separator + ".cred.conf");
        file.createNewFile();
        Files.write(file.toPath(), lines, Charset.forName("UTF-8"));
        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.GROUP_READ);
        Files.setPosixFilePermissions(file.toPath(), perms);
    }

    public String newPassword() {
        System.out.print("Enter a New password: ");
        char[] pass = System.console().readPassword();
        return new String(pass);
        // try {
        // return this.bufferedReader.readLine();
        // } catch (IOException e) {
        // return null;
        // }
    }

    public void newPrompt() {
        try {
            while (true) {
                this.print();
            }
        } catch (IOException e) {
            e.printStackTrace();
            AppLogger.getInstance().error(ErrorCode.CLI_READLINE + " " + e.getMessage());
        }
    }

    public void out(String s) {
        System.out.println(s);
    }

    private void print() throws IOException {
        if (App.checkForExistence(this.feedback)) {
            String log = this.feedback + " (" + (System.currentTimeMillis() - this.start) + "ms)";
            System.out.println(log);
            AppLogger.getInstance().info(log);
        }
        if (App.checkForExistence(this.table))
            System.out.println(this.table);
        this.table = null;
        System.out.print(App.PS1);
        this.scan();
    }

    public void run() {
        this.newPrompt();
    }

    private void scan() throws IOException {
        String temp = this.bufferedReader.readLine();
        AppLogger.getInstance().info("NEW REQUEST: <" + temp + ">");
        this.start = System.currentTimeMillis();
        if (App.checkForExistence(temp) && temp.trim().equals("exit")) {
            this.cliController.end();
        } else
            this.feedback = this.cliController.newInput(temp);
    }

    public void setTable(String table) {
        this.table = table;
    }

}
