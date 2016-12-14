package plugins.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import controller.DBMSController;
import model.statements.Viewable;
import util.App;

public class Statement implements java.sql.Statement {

    private boolean closed;
    private List<String> commands;
    private Connection connection;
    private DBMSController dbmsController;
    private ResultSet resultSet;

    public Statement(Connection connection, DBMSController dbmsController) {
        this.commands = new ArrayList<>();
        this.dbmsController = dbmsController;
        this.connection = connection;
        this.closed = false;
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        if (this.isClosed())
            throw new SQLException("Connection is closed");
        this.commands.add(sql);
    }

    @Override
    public void cancel() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearBatch() throws SQLException {
        if (this.isClosed())
            throw new SQLException("Connection is closed");
        this.commands.clear();
    }

    @Override
    public void clearWarnings() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws SQLException {
        if (App.checkForExistence(this.commands)) {
            this.commands.clear();
            this.commands = null;
        }
        this.restResultSet();
        this.closed = true;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        this.handleExecute();
        try {
            this.dbmsController.getSQLParserController().parse(sql);
        } catch (ParseException | RuntimeException e) {
            throw new SQLException(e.getMessage());
        }
        if (this.dbmsController.getSQLParserController().getSqlParserHelper()
                .getCurrentQuery() instanceof Viewable) {
            resultSet = new plugins.jdbc.ResultSet(this,
                    this.dbmsController.getDatabaseController().getHelper().getSelectedTable());
            if (this.resultSet.first()) { // checks whether the set is empty or not.
                this.resultSet.previous();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean execute(String arg0, int arg1) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean execute(String arg0, int[] arg1) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean execute(String arg0, String[] arg1) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        this.handleExecute();
        int[] result = new int[this.commands.size()];
        for (int i = 0; i < result.length; i++) {
            try {
                this.execute(this.commands.get(i));
                if (App.checkForExistence(this.dbmsController.getDatabaseController().getHelper()
                        .getSelectedTable())) {
                    result[i] = 0;
                } else if (this.dbmsController.getSQLParserController().getSqlParserHelper()
                        .getCurrentQuery() instanceof Viewable)
                    result[i] = java.sql.Statement.SUCCESS_NO_INFO;
                else
                    result[i] = this.dbmsController.getDatabaseController().getHelper()
                            .getSelectedTable().getNoOfAffectedRecords();
            } catch (SQLException e) {
                result[i] = java.sql.Statement.EXECUTE_FAILED;
            }
        }
        return result;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        this.handleExecute();
        this.execute(sql);
        if (!(this.dbmsController.getSQLParserController().getSqlParserHelper()
                .getCurrentQuery() instanceof Viewable))
            throw new SQLException();
        this.resultSet = new plugins.jdbc.ResultSet(this,
                this.dbmsController.getDatabaseController().getHelper().getSelectedTable());
        return this.resultSet;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        this.handleExecute();
        this.execute(sql);
        if (this.dbmsController.getSQLParserController().getSqlParserHelper()
                .getCurrentQuery() instanceof Viewable)
            throw new SQLException();
        if (!App.checkForExistence(
                this.dbmsController.getDatabaseController().getHelper().getSelectedTable()))
            return 0;
        return this.dbmsController.getDatabaseController().getHelper().getSelectedTable()
                .getNoOfAffectedRecords();
    }

    @Override
    public int executeUpdate(String arg0, int arg1) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int executeUpdate(String arg0, int[] arg1) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int executeUpdate(String arg0, String[] arg1) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (this.isClosed())
            throw new SQLException("Connection is closed");
        return this.connection;
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
    public ResultSet getGeneratedKeys() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxRows() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getMoreResults(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        if (this.isClosed())
            throw new SQLException("Connection is closed");
        return this.resultSet;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getResultSetType() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        if (this.isClosed())
            throw new SQLException("Connection is closed");
        if (this.dbmsController.getSQLParserController().getSqlParserHelper()
                .getCurrentQuery() instanceof Viewable)
            return -1;
        if (App.checkForExistence(
                this.dbmsController.getDatabaseController().getHelper().getSelectedTable()))
            return this.dbmsController.getDatabaseController().getHelper().getSelectedTable()
                    .getNoOfAffectedRecords();
        return 0;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throw new UnsupportedOperationException();
    }

    private void handleExecute() throws SQLException {
        if (this.isClosed())
            throw new SQLException("Connection is closed");
        this.restResultSet();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.closed;
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPoolable() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    private void restResultSet() throws SQLException {
        if (App.checkForExistence(this.resultSet)) {
            this.resultSet.close();
            this.resultSet = null;
        }
    }

    @Override
    public void setCursorName(String arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEscapeProcessing(boolean arg0) throws SQLException {
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
    public void setMaxFieldSize(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxRows(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPoolable(boolean arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setQueryTimeout(int arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        throw new UnsupportedOperationException();
    }

}
