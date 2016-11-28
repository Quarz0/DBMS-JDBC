package controller;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import model.Record;
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

    public SQLParserController() {};
    
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
        if (App.checkForExistence(s))
            this.callForFailure();
        groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_WHERE);
        if (App.checkForExistence(groups))
            this.callForFailure();
        whereExists = App.checkForExistence(groups[Regex.PARSE_WITH_WHERE_GROUP_ID]);
        query = this.locateQuery(groups[1].trim());
        if (App.checkForExistence(query))
            this.callForFailure();
        if (whereExists) {
            where = new Where(groups[Regex.PARSE_WITH_WHERE_GROUP_ID + 1]);
            //where.parse(groups[Regex.PARSE_WITH_WHERE_GROUP_ID + 1]);
            query.parse(groups[Regex.PARSE_WITH_WHERE_GROUP_ID - 1]);
        } else {
            query.parse(groups[Regex.PARSE_WITH_WHERE_GROUP_ID + 1]);
        }
        //this.sqlParserHelper.setCurrentQuery(query, where);
        w = where;
    }

    private void callForFailure(/* Exception e */) {

    }

    public static Where w;
    public static void main(String[] args) {
        String statement = "select * from table where age >= (-1 * -19 + 11) and ( not (1== 2)) or (name==\"and\")";
        List<String> cols = new ArrayList<>();
        cols.add("name"); cols.add("Age"); cols.add("Country");
        List<Object> vals = new ArrayList<>();
        vals.add("and"); vals.add(19); vals.add("Egypt");
        
        Record r = new Record(cols, vals);
        SQLParserController s = new SQLParserController();
        s.parse(statement);
        System.out.println(w.getExpression());
        try {
            System.out.println(w.evaluate(r));
        } catch (ScriptException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
