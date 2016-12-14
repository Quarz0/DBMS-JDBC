package eg.edu.alexu.csd.oop.DBMS.controller;

import java.text.ParseException;

import eg.edu.alexu.csd.oop.DBMS.model.SQLParserHelper;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Distinct;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Order;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Query;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Where;
import eg.edu.alexu.csd.oop.DBMS.util.App;
import eg.edu.alexu.csd.oop.DBMS.util.Regex;
import eg.edu.alexu.csd.oop.DBMS.util.RegexEvaluator;

public class SQLParserController {

    @SuppressWarnings("unused")
    private DBMSController dbmsController;
    private SQLParserHelper sqlParserHelper;

    public SQLParserController() {
    };

    public SQLParserController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
        this.sqlParserHelper = new SQLParserHelper(dbmsController);
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

    public void parse(String s) throws ParseException, RuntimeException {
        if (!App.checkForExistence(s))
            throw new ParseException("Syntax error!", 0);
        s = App.replace(App.replace(s, "(", " ("), ")", ") ").trim();
        Query query = this.locateQuery(s.trim().split(" ")[0]);
        if (!App.checkForExistence(query))
            throw new ParseException("Syntax error!", 0);

        String[] groups;
        int queryStartIndex = s.indexOf(" ");
        if (App.checkForExistence(groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_DISTINCT))) {
            Distinct d = new Distinct();
            d.parse(groups[1]);
            query.addClause(d);
            queryStartIndex = s.toLowerCase().indexOf("distinct") + "distinct".length();
        }
        if (App.checkForExistence(groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_ORDER_BY))) {
            Order o = new Order();
            o.parse(groups[2] != null ? groups[2] : groups[4]);
            query.addClause(o);
        }
        if (App.checkForExistence(groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_WHERE))) {
            Where w = new Where();
            w.parse(groups[2] != null ? groups[2] : groups[4]);
            query.addClause(w);
        }

        query.parse(s.substring(queryStartIndex,
                Math.min(RegexEvaluator.startIndex(s, Regex.PARSE_WITH_ORDER_BY),
                        RegexEvaluator.startIndex(s, Regex.PARSE_WITH_WHERE))));
        this.sqlParserHelper.setCurrentQuery(query);
    }

}
