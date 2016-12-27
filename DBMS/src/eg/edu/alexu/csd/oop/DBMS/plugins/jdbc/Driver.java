package eg.edu.alexu.csd.oop.DBMS.plugins.jdbc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import eg.edu.alexu.csd.oop.DBMS.model.BackEndWriterFactory;
import eg.edu.alexu.csd.oop.DBMS.util.App;
import eg.edu.alexu.csd.oop.DBMS.util.MessageDigestUtil;
import eg.edu.alexu.csd.oop.DBMS.util.RegexEvaluator;

public class Driver implements java.sql.Driver {

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        if (!App.checkForExistence(url)) {
            throw new SQLException();
        } else if (url.matches("jdbc:[a-zA-Z]+db://localhost")) {
            if (App.checkForExistence(BackEndWriterFactory.getBackEndWriter(
                    RegexEvaluator.evaluate(url, "jdbc:([a-zA-Z]+)db://localhost")[1])))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    private boolean canLogIn(String username, String password, String path) throws SQLException {
        if (App.checkForExistence(username) && App.checkForExistence(password)) {
            try {
                FileReader reader = new FileReader(path + ".cred.conf");
                Properties props = new Properties();
                props.load(reader);
                String savedUsername = props.getProperty("username", null);
                String savedPassword = props.getProperty("password", null);
                if (!App.checkForExistence(username) || !App.checkForExistence(password)) {
                    return false;
                }
                reader.close();
                try {
                    if (username.equals(savedUsername) && savedPassword
                            .equals(MessageDigestUtil.getSecuredPassword(password))) {
                        return true;
                    }
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException();
                } catch (RuntimeException e) {
                    throw new RuntimeException();
                }
            } catch (IOException ex) {
                throw new SQLException("Error!");
            }
        }
        return false;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        try {
            if (!acceptsURL(url)) {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
        String username = info.getProperty("username", null);
        String password = info.getProperty("password", null);
        String path = info.getProperty("path");
        try {
            if (!canLogIn(username, password, path)) {
                return null;
            }
        } catch (Exception e) {
        }
        if (!App.checkForExistence(info.get("path"))) {
            throw new SQLException();
        }
        File appDir = new File(info.get("path").toString());
        try {
            String writerType = url.substring(url.indexOf(':') + 1, url.lastIndexOf(':'));
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            return new eg.edu.alexu.csd.oop.DBMS.plugins.jdbc.Connection(appDir.getPath(),
                    BackEndWriterFactory
                            .getBackEndWriter(writerType.substring(0, writerType.length() - 2))); // the
                                                                                                  // url
                                                                                                  // is
                                                                                                  // on
                                                                                                  // the
                                                                                                  // form
                                                                                                  // ***db
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    @Override
    public int getMajorVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMinorVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        DriverPropertyInfo[] ret = new DriverPropertyInfo[2];
        ret[0] = new DriverPropertyInfo("username", info.getProperty("username", null));
        ret[1] = new DriverPropertyInfo("password", info.getProperty("password", null));
        return ret;
    }

    @Override
    public boolean jdbcCompliant() {
        throw new UnsupportedOperationException();
    }

}