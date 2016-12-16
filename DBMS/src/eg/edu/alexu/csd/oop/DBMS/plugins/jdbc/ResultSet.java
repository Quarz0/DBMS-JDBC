package eg.edu.alexu.csd.oop.DBMS.plugins.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eg.edu.alexu.csd.oop.DBMS.model.Record;
import eg.edu.alexu.csd.oop.DBMS.model.SelectionTable;
import eg.edu.alexu.csd.oop.DBMS.util.App;

public class ResultSet implements java.sql.ResultSet {

    private int cursor;
    private boolean isClosed;
    private eg.edu.alexu.csd.oop.DBMS.plugins.jdbc.ResultSetMetaData metaData;
    private List<Record> recordList;
    private Statement statement;
    private SelectionTable table;

    public ResultSet(Statement statement, SelectionTable table) {
        if (App.checkForExistence(table)) {
            this.table = table;
        } else {
            this.table = new SelectionTable("", new LinkedHashMap<String, Class<?>>());
        }
        this.recordList = table.getRecordList();
        this.cursor = 0;
        this.isClosed = false;
        this.metaData = new eg.edu.alexu.csd.oop.DBMS.plugins.jdbc.ResultSetMetaData(table);
        this.statement = statement;
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        checkClosed();
        if (row >= 0) {
            cursor = Math.min(row, recordList.size() + 1);
        } else {
            cursor = Math.max(0, recordList.size() + 1 - row);
        }
        return cursor >= 1 && cursor <= recordList.size();
    }

    @Override
    public void afterLast() throws SQLException {
        checkClosed();
        if (recordList.size() != 0)
            cursor = recordList.size() + 1;
    }

    @Override
    public void beforeFirst() throws SQLException {
        checkClosed();
        if (recordList.size() != 0)
            cursor = 0;
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw new UnsupportedOperationException();

    }

    private void checkClosed() throws SQLException {
        if (isClosed()) {
            throw new SQLException();
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void close() throws SQLException {
        isClosed = true;
    }

    @Override
    public void deleteRow() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        checkClosed();
        int i = 1;
        for (String column : table.getHeader().keySet()) {
            if (column.equals(columnLabel.toLowerCase())) {
                return i;
            }
            i++;
        }
        throw new SQLException();
    }

    @Override
    public boolean first() throws SQLException {
        checkClosed();
        return absolute(1);
    }

    @Override
    public Array getArray(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Array getArray(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public InputStream getAsciiStream(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public InputStream getAsciiStream(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public BigDecimal getBigDecimal(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public BigDecimal getBigDecimal(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public BigDecimal getBigDecimal(String arg0, int arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public InputStream getBinaryStream(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public InputStream getBinaryStream(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Blob getBlob(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Blob getBlob(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean getBoolean(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean getBoolean(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public byte getByte(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public byte getByte(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public byte[] getBytes(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public byte[] getBytes(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Reader getCharacterStream(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Reader getCharacterStream(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Clob getClob(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Clob getClob(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public int getConcurrency() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public String getCursorName() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        if (!App.checkForExistence(getValue(columnIndex))) {
            return null;
        }
        try {
            return (Date) getValue(columnIndex);
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    @Override
    public Date getDate(int arg0, Calendar arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        return getDate(findColumn(columnLabel));
    }

    @Override
    public Date getDate(String arg0, Calendar arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public double getDouble(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public double getDouble(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public int getFetchDirection() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public int getFetchSize() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        if (!App.checkForExistence(getValue(columnIndex))) {
            return 0;
        }
        try {
            return (Float) getValue(columnIndex);
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        return getFloat(findColumn(columnLabel));
    }

    @Override
    public int getHoldability() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        if (!App.checkForExistence(getValue(columnIndex))) {
            return 0;
        }
        try {
            return (int) getValue(columnIndex);
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        return getInt(findColumn(columnLabel));
    }

    @Override
    public long getLong(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public long getLong(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        checkClosed();
        return metaData;
    }

    @Override
    public Reader getNCharacterStream(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Reader getNCharacterStream(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public NClob getNClob(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public NClob getNClob(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public String getNString(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public String getNString(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        if (!App.checkForExistence(getValue(columnIndex))) {
            return null;
        }
        return getValue(columnIndex);
    }

    @Override
    public <T> T getObject(int arg0, Class<T> arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Object getObject(int arg0, Map<String, Class<?>> arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Object getObject(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public <T> T getObject(String arg0, Class<T> arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Object getObject(String arg0, Map<String, Class<?>> arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Ref getRef(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Ref getRef(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public int getRow() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public RowId getRowId(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public RowId getRowId(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public short getShort(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public short getShort(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public SQLXML getSQLXML(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public SQLXML getSQLXML(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Statement getStatement() throws SQLException {
        checkClosed();
        return this.statement;
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        if (!App.checkForExistence(getValue(columnIndex))) {
            return null;
        }
        try {
            return (String) getValue(columnIndex);
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        return getString(findColumn(columnLabel));
    }

    @Override
    public Time getTime(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Time getTime(int arg0, Calendar arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Time getTime(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Time getTime(String arg0, Calendar arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Timestamp getTimestamp(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Timestamp getTimestamp(int arg0, Calendar arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Timestamp getTimestamp(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public Timestamp getTimestamp(String arg0, Calendar arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public int getType() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public InputStream getUnicodeStream(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public InputStream getUnicodeStream(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public URL getURL(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public URL getURL(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    private Object getValue(int columnIndex) throws SQLException {
        checkClosed();
        if (columnIndex < 1 || columnIndex > table.getHeader().size() || cursor < 1
                || cursor > recordList.size()) {
            throw new SQLException();
        }
        Object val = recordList.get(cursor - 1).getValues().get(columnIndex - 1);
        return val;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void insertRow() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean isAfterLast() throws SQLException {
        checkClosed();
        return cursor == recordList.size() + 1;
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        checkClosed();
        return cursor == 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return isClosed;
    }

    @Override
    public boolean isFirst() throws SQLException {
        checkClosed();
        return cursor == 1;
    }

    @Override
    public boolean isLast() throws SQLException {
        checkClosed();
        return cursor == recordList.size();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean last() throws SQLException {
        checkClosed();
        return absolute(-1);
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean next() throws SQLException {
        checkClosed();
        return absolute(cursor + 1);
    }

    @Override
    public boolean previous() throws SQLException {
        checkClosed();
        return absolute(cursor - 1);
    }

    @Override
    public void refreshRow() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean relative(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean rowDeleted() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean rowInserted() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean rowUpdated() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void setFetchDirection(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void setFetchSize(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public String toString() {
        return this.table.toString();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateArray(int arg0, Array arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateArray(String arg0, Array arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateAsciiStream(int arg0, InputStream arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateAsciiStream(int arg0, InputStream arg1, int arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateAsciiStream(String arg0, InputStream arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateAsciiStream(String arg0, InputStream arg1, int arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateAsciiStream(String arg0, InputStream arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBigDecimal(String arg0, BigDecimal arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBinaryStream(int arg0, InputStream arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBinaryStream(int arg0, InputStream arg1, int arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBinaryStream(String arg0, InputStream arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBinaryStream(String arg0, InputStream arg1, int arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBinaryStream(String arg0, InputStream arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBlob(int arg0, Blob arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBlob(int arg0, InputStream arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBlob(int arg0, InputStream arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBlob(String arg0, Blob arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBlob(String arg0, InputStream arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBlob(String arg0, InputStream arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBoolean(int arg0, boolean arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBoolean(String arg0, boolean arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateByte(int arg0, byte arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateByte(String arg0, byte arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBytes(int arg0, byte[] arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateBytes(String arg0, byte[] arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateCharacterStream(int arg0, Reader arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateCharacterStream(int arg0, Reader arg1, int arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateCharacterStream(String arg0, Reader arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateCharacterStream(String arg0, Reader arg1, int arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateClob(int arg0, Clob arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateClob(int arg0, Reader arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateClob(int arg0, Reader arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateClob(String arg0, Clob arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateClob(String arg0, Reader arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateClob(String arg0, Reader arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateDate(int arg0, Date arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateDate(String arg0, Date arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateDouble(int arg0, double arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateDouble(String arg0, double arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateFloat(int arg0, float arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateFloat(String arg0, float arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateInt(int arg0, int arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateInt(String arg0, int arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateLong(int arg0, long arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateLong(String arg0, long arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNCharacterStream(int arg0, Reader arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNCharacterStream(String arg0, Reader arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNClob(int arg0, NClob arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNClob(int arg0, Reader arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNClob(int arg0, Reader arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNClob(String arg0, NClob arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNClob(String arg0, Reader arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNClob(String arg0, Reader arg1, long arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNString(int arg0, String arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNString(String arg0, String arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNull(int arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateNull(String arg0) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateObject(int arg0, Object arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateObject(int arg0, Object arg1, int arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateObject(String arg0, Object arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateObject(String arg0, Object arg1, int arg2) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateRef(int arg0, Ref arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateRef(String arg0, Ref arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateRow() throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateRowId(int arg0, RowId arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateRowId(String arg0, RowId arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateShort(int arg0, short arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateShort(String arg0, short arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateString(int arg0, String arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateString(String arg0, String arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateTime(int arg0, Time arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateTime(String arg0, Time arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateTimestamp(int arg0, Timestamp arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void updateTimestamp(String arg0, Timestamp arg1) throws SQLException {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean wasNull() throws SQLException {
        throw new UnsupportedOperationException();

    }

}