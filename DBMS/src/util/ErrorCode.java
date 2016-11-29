package util;

public final class ErrorCode {

    public static final String CLI_READLINE = "HMSF0";
    public static final String CLI_CLOSE = "HMSF1";

    public static final String SYNTAX_ERROR = "Syntax error!";
    public static final String QUERY_IS_OK = "Query is ok";
    public static final String QUERY_IS_NOT_OK = "Query failed";
    public static final String DATABASE_DUPLICATION = "Database already exists!";
    public static final String DATABASE_NOT_FOUND = "Database doesn't exist!";
    public static final String DATABASE_IS_NOT_SELECTED = "No database selected!";
    public static final String TABLE_NOT_FOUND = "Cannot find table!";
    public static final String WRONG_DATA = "Wrong data";
    public static final String ERROR = "ERROR!";

    public static final String LOCATE_QUERY = "Query not found!";
    public static final String FAILED_TO_CREATE_DEFAULT_DATABASE = "Cannot create workspace directory";

    private ErrorCode() {
    }
}
