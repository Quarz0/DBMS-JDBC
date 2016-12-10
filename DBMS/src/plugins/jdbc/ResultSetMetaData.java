package plugins.jdbc;

import java.sql.SQLException;

import javax.xml.transform.Result;

public class ResultSetMetaData implements java.sql.ResultSetMetaData, Result {

    @Override
    public String getCatalogName(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getColumnClassName(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnCount() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getColumnDisplaySize(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getColumnLabel(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getColumnName(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getColumnType(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getColumnTypeName(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPrecision(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getScale(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSchemaName(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSystemId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTableName(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAutoIncrement(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCaseSensitive(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCurrency(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDefinitelyWritable(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int isNullable(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReadOnly(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSearchable(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSigned(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWritable(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSystemId(String arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

}
