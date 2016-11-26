package util;

public final class Regex {

    public static final String PARSE_WITH_WHERE = "(\\w+)\\s+((.*)\\s+(WHERE\\s))?(.*)";
    public static final int PARSE_WITH_WHERE_GROUP_ID = 4;

    public static final String PARSE_WITH_CREATE_DATABASE = "\\A\\s*DATABASE\\s+([a-zA-Z_]\\S*)\\s*\\Z";

    public static final String PARSE_WITH_CREATE_TABLE = "\\A\\s*TABLE\\s+([a-zA-Z_]\\w*)\\s*\\(\\s*([a-zA-Z_]\\w*\\s+[a-zA-Z]+\\s*)(,\\s*[a-zA-Z_]\\w*\\s+[a-zA-Z]+\\s*)*\\)\\Z";

    private Regex() {
    }
}
