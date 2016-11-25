package controller;

import model.SQLParserHelper;
import model.statements.Query;
import model.statements.Where;
import util.ErrorCode;
import util.Regex;
import util.RegexEvaluator;

public class SQLParserController {

    private DBMSController dbmsController;
    private SQLParserHelper sqlParserHelper;

    public SQLParserController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        this.sqlParserHelper = new SQLParserHelper(dbmsController);
    }

    private Query loacteQuery(String queryIdentifier) {
        Class<?> cls;
        Query query;
        try {
            cls = Class.forName("model.statements." + queryIdentifier.substring(0, 1).toUpperCase()
                    + queryIdentifier.substring(1).toLowerCase());
            query = (Query) cls.getConstructor().newInstance();
        } catch (Exception e) {
            System.out.println(ErrorCode.LOCATE_QUERY);
            e.printStackTrace();
            return null;
        }
        if (Query.class.isAssignableFrom(cls))
            return query;
        else
            return null;
    }

    public void parse(String s) {
        Query query = null;
        Where where = null;
        String[] groups;
        boolean whereExists = false;
        if (s.equals(null) || s.trim().isEmpty())
            this.callForFailure();
        groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_WHERE);
        if (groups.equals(null))
            this.callForFailure();
        whereExists = !groups[Regex.PARSE_WITH_WHERE_GROUP_ID].equals(null);
        query = this.loacteQuery(groups[1].trim());
        if (query.equals(null))
            this.callForFailure();
        if (whereExists) {
            where = new Where();
            where.parse(groups[Regex.PARSE_WITH_WHERE_GROUP_ID + 1]);
            query.parse(groups[Regex.PARSE_WITH_WHERE_GROUP_ID - 1]);
        } else {
            query.parse(groups[Regex.PARSE_WITH_WHERE_GROUP_ID + 1]);
        }
        this.sqlParserHelper.setCurrentQuery(query, where);
    }

    private void callForFailure(/* Exception e */) {

    }

}
