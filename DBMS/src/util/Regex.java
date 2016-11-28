package util;

public final class Regex {

    public static final String LEGAL_IDENTIFIER = "[a-zA-Z_]\\w*";

    public static final String PARSE_WITH_WHERE = "(\\w+)\\s+((.*)\\s+(WHERE\\s))?(.*)";
    public static final int PARSE_WITH_WHERE_GROUP_ID = 4;

    public static final String PARSE_WITH_CREATE_DATABASE = "\\A\\s*DATABASE\\s+([a-zA-Z_]\\w*)\\s*\\Z";

    public static final String PARSE_WITH_CREATE_TABLE = "\\A\\s*TABLE\\s+([a-zA-Z_]\\w*)\\s*\\(\\s*([a-zA-Z_]\\w*\\s+[a-zA-Z]+\\s*(,\\s*[a-zA-Z_]\\w*\\s+[a-zA-Z]+\\s*)*)\\)\\Z";

    public static final String PARSE_WITH_DELETE_ALL = "\\A\\s*(\\*)?\\s+FROM\\s+([a-zA-Z_]\\w*)\\s*\\Z";

    public static final String PARSE_WITH_DROP_DATABASE = "\\A\\s*DATABASE\\s+([a-zA-Z_]\\w*)\\s*\\Z";
    public static final String PARSE_WITH_DROP_TABLE = "\\A\\s*TABLE\\s+([a-zA-Z_]\\w*)\\s*\\Z";

    public static final String PARSE_WITH_SELECT_FROM = "\\A\\s*(.*?)\\s+FROM\\s+([a-zA-Z_]\\w*)\\s*\\Z";
    public static final String PARSE_WITH_SELECT_ALL_FROM = "\\A\\s*\\*?\\s+FROM\\s+([a-zA-Z_]\\w*)\\s*\\Z";

    public static final String PARSE_WITH_USE = "\\A\\s*DATABASE\\s+([a-zA-Z_]\\w*)\\s*\\Z";

    public static final String PARSE_WITH_INSERT = "\\A\\s*INTO\\s+([a-zA-Z_]\\w*)(?:\\(\\s*([a-zA-Z_]\\w*\\s*(?:,\\s*[a-zA-Z_]\\w*\\s*)*)\\))?\\s+VALUES\\s*\\((.+)\\)\\s*\\Z";
    public static final String PARSE_WITH_INSERT_TRIM_MATCH = ".+(?<!,)";
    public static final String PARSE_WITH_INSERT_SPLIT_PATTERN1 = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public static final String PARSE_WITH_INSERT_SPLIT_PATTERN2 = ",(?=(?:[^\']*\'[^\']*\')*[^\']*$)";

    public static final String PARSE_WITH_UPDATE = "\\A\\s*([a-zA-Z_]\\w*)\\s+SET\\s+(.+)\\Z";
    public static final String PARSE_WITH_UPDATE_TRIM_MATCH = ".+(?<!,)";
    public static final String PARSE_WITH_UPDATE_SPLIT_PATTERN1 = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public static final String PARSE_WITH_UPDATE_SPLIT_PATTERN2 = ",(?=(?:[^\']*\'[^\']*\')*[^\']*$)";
    public static final String PARSE_WITH_UPDATE_SPLIT_PATTERN_LEFT = "\\A\\s*([a-zA-Z_]\\w*)\\s*=\\s*(.*)\\Z";
    public static final String PARSE_WITH_UPDATE_SPLIT_PATTERN_RIGHT = "\\A\\s*(.*)=\\s*([a-zA-Z_]\\w*)\\Z";
    public static final String PARSE_WITH_UPDATE_TRIM_MATCH_LEFT = "\\A\\s*"
            + Regex.LEGAL_IDENTIFIER + "\\s*=\\s*.*\\Z";
    public static final String PARSE_WITH_UPDATE_TRIM_MATCH_RIGHT = "\\A\\s*.*=\\s*"
            + Regex.LEGAL_IDENTIFIER + "\\Z";

    private Regex() {
    }
}