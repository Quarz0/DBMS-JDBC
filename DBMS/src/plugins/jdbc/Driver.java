package plugins.jdbc;

import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class Driver implements java.sql.Driver {

    @Override
    public boolean acceptsURL(String arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Connection connect(String arg0, Properties arg1) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String arg0, Properties arg1) throws SQLException {
        // TODO Auto-generated method stub
        return null;
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
    public boolean jdbcCompliant() {
        throw new UnsupportedOperationException();
    }

}
