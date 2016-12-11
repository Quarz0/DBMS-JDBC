package plugins.jdbc;

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
import java.util.List;
import java.util.Map;

import model.Record;
import model.SelectionTable;
import util.App;

public class ResultSet implements java.sql.ResultSet {

    private int cursor;
    private boolean isClosed;
    private plugins.jdbc.ResultSetMetaData metaData;
    private List<Record> recordList;
    private SelectionTable table;

    public ResultSet(SelectionTable table) {
        this.table = table;
        this.recordList = table.getRecordList();
        this.cursor = 0;
        this.isClosed = false;
        this.metaData = new plugins.jdbc.ResultSetMetaData(table);
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
        // TODO Auto-generated method stub

    }

    private void checkClosed() throws SQLException {
        if (isClosed()) {
            throw new SQLException();
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() throws SQLException {
        isClosed = true;
    }

    @Override
    public void deleteRow() throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        checkClosed();
        int i = 1;
        for (String column : table.getHeader().keySet()) {
            if (column.equals(columnLabel)) {
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
    public Array getArray(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getConcurrency() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getCursorName() throws SQLException {
        // TODO Auto-generated method stub
        return null;
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
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        return getDate(findColumn(columnLabel));
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getFetchDirection() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getFetchSize() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
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
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        if (!App.checkForExistence(getValue(columnIndex))) {
            return 0;
        }
        try {
            return (int) getValue(columnIndex);
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        return getInt(findColumn(columnLabel));
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        checkClosed();
        return metaData;
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        if (!App.checkForExistence(getValue(columnIndex))) {
            return null;
        }
        return getValue(columnIndex);
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getRow() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Statement getStatement() throws SQLException {
        checkClosed();
        // TODO Auto-generated method stub
        return null;
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
    public Time getTime(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getType() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URL getURL(String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void insertRow() throws SQLException {
        // TODO Auto-generated method stub

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
