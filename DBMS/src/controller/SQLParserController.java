package controller;

import model.statements.Query;
import model.statements.Where;
import util.RegexEvaluator;

public class SQLParserController {

	private DBMSController dbmsController;

	public SQLParserController(DBMSController dbmsController) {
		this.dbmsController = dbmsController;
	}

	private Query getQuery(String q) {
	    Class<?> cls;
	    Query query;
        try {
            cls = Class.forName ("model.statements." + q.substring(0, 1).toUpperCase() + q.substring(1).toLowerCase());
            query = (Query) cls.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
	    return Query.class.isAssignableFrom(cls) ? query : null;
	}
	
	public void parse(String s) {
	    String wherePattern = "(\\w+)(\\s+)(.*?)(\\s+)(WHERE)(\\s+)(.*)";
	    String normalPattern = "(\\w+)(\\s+)(.*)";
	    String[] g = RegexEvaluator.evaluate(s, wherePattern);
	    if (g == null)
	        g = RegexEvaluator.evaluate(s, normalPattern);
	    Query query = null;
	    if (g != null) {
	        query = getQuery(g[1]);
	        System.out.println("HERE " + g[3]);
            query.parse(g[3]);
	    }
	    else {
	        System.out.println("error");
	        return;
	    }
//	    if (g1 != null) {
//	        // TODO
//    	       Where where = new Where();
//            //   where.parse(g1[7]);
//	    }
	    
	}
	
	// for testing
	public static void main(String[] args) {
	    SQLParserController s = new SQLParserController(null);
	    s.parse("Select  _121 , A+abde From table1 fsdf");
	}

}
