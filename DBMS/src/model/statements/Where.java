package model.statements;

import javax.script.ScriptException;

import model.Record;
import util.App;
import util.BooleanEvaluator;


public class Where implements Clause {
   
    private String expression;
    
    public Where(String s) {
        expression = s;
    }

    public boolean evaluate(Record record) throws ScriptException {
        String exp = getFilledExpression(record);
        return BooleanEvaluator.evaluate(exp);
    }
    
    private String getFilledExpression(Record record) {
        String exp = expression;
        for (int i = 0; i < record.getColumns().size(); i++) {
            if (record.getValues().get(i) instanceof String) {
                exp = App.replace(exp, record.getColumns().get(i).toLowerCase(), "\"" + record.getValues().get(i).toString() + "\"");
            }
            else {
                exp = App.replace(exp, record.getColumns().get(i).toLowerCase(), record.getValues().get(i).toString());
            }
        }
        return exp;
    }
    
    public void setColumns() {
        
    }


    private void callForFailure(/* Exception e */) {

    }

    public String getExpression() {
        return expression;
    }
    
    public void setExpression(String exp) {
        expression = exp;
    }

}
