package controller;

import java.text.ParseException;

import model.SQLParserHelper;
import model.statements.Distinct;
import model.statements.Order;
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

    public void callForFailure(String errorMessage) {
        this.dbmsController.getCLIController().callForFailure(errorMessage);
    }

    public SQLParserHelper getSqlParserHelper() {
        return sqlParserHelper;
    }

    private Query locateQuery(String queryIdentifier) {
        Class<?> cls;
        Query query;
        try {
            cls = Class.forName("model.statements." + queryIdentifier.substring(0, 1).toUpperCase()
                    + queryIdentifier.substring(1).toLowerCase());
            query = (Query) cls.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
        if (Query.class.isAssignableFrom(cls))
            return query;
        else
            return null;
    }

    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s))
            throw new ParseException("Invalid", 0);
        s = App.replace(App.replace(s, "(", " ("), ")", ") ").toLowerCase();
        Query query = this.locateQuery(s.trim().split(" ")[0]);
        if (!App.checkForExistence(query))
            throw new ParseException("Invalid", 0);

        String[] groups;
        String queryParse = s.substring(s.trim().indexOf(" "));
        if (App.checkForExistence(groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_DISTINCT))) {
            Distinct d = new Distinct();
            d.parse(groups[1]);
            query.addClause(d);
            queryParse = groups[1];
        }
        if (App.checkForExistence(groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_ORDER_BY))) {
            Order o = new Order();
            o.parse(groups[2]);
            query.addClause(o);
            queryParse = groups[1];
        }
        if (App.checkForExistence(groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_WHERE))) {
            Where w = new Where();
            w.parse(groups[2]);
            query.addClause(w);
            queryParse = groups[1];
        }
        
        query.parse(queryParse.trim());
        this.sqlParserHelper.setCurrentQuery(query);
    }

}
