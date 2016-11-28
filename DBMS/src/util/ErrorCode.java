package util;

public final class ErrorCode {

    public static final String CLI_READLINE = "HMSF0";
    public static final String CLI_CLOSE = "HMSF1";

    public static final String SYNTAX_ERROR = "Syntax error!";

    public static final String LOCATE_QUERY = "Query not found!";
    public static final String FAILED_TO_CREATE_DEFAULT_DATABASE = "Cannot create workspace directory";

    private ErrorCode() {
    }
}
