package controller;

import model.SQLParserHelper;
import model.statements.Query;
import model.statements.Where;
import util.App;
import util.ErrorCode;
import util.Regex;
import util.RegexEvaluator;

public class SQLParserController {

    private DBMSController dbmsController;
    private SQLParserHelper sqlParserHelper;

    public SQLParserController() {
    };

    public SQLParserController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        this.sqlParserHelper = new SQLParserHelper(dbmsController);
    }

    private Query locateQuery(String queryIdentifier) {
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
        boolean errorExists = false;
        if (!App.checkForExistence(s))
            this.callForFailure(ErrorCode.SYNTAX_ERROR);
        groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_WHERE);
        if (!App.checkForExistence(groups))
            this.callForFailure(ErrorCode.SYNTAX_ERROR);
        whereExists = App.checkForExistence(groups[Regex.PARSE_WITH_WHERE_GROUP_ID]);
        query = this.locateQuery(groups[1].trim());
        if (!App.checkForExistence(query))
            this.callForFailure(ErrorCode.SYNTAX_ERROR);
        if (whereExists) {
            where = new Where(groups[Regex.PARSE_WITH_WHERE_GROUP_ID + 1]);
            errorExists = query.parse(groups[Regex.PARSE_WITH_WHERE_GROUP_ID - 1]);
            query.setClause(where);
        } else {
            errorExists = query.parse(groups[Regex.PARSE_WITH_WHERE_GROUP_ID + 1]);
        }
        if (!errorExists)
            this.sqlParserHelper.setCurrentQuery(null);
        else
            this.sqlParserHelper.setCurrentQuery(query);
    }

    public void callForFailure(String errorMessage) {
        this.dbmsController.getCLIController().callForFailure(errorMessage);
    }

    public SQLParserHelper getSqlParserHelper() {
        return sqlParserHelper;
    }

}
