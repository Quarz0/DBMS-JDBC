package controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.script.ScriptException;

import org.apache.commons.lang3.builder.CompareToBuilder;

import model.Record;
import model.SelectionTable;
import model.TypeFactory;
import model.statements.Delete;
import model.statements.Select;
import model.statements.Update;
import util.App;
import util.BooleanEvaluator;

public class ClauseController implements DBMSClause {

    private DBMSController dbmsController;

    public ClauseController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
    }

    @Override
    public void distinct() throws RuntimeException {
        SelectionTable table = this.dbmsController.getDatabaseController().getHelper()
                .getSelectedTable();
        Set<Record> distinctRecords = new LinkedHashSet<>();
        int i = 0;
        while (i < table.getRecordList().size()) {
            if (distinctRecords.contains(table.getRecordList().get(i))) {
                table.getRecordList().remove(i);
            } else {
                distinctRecords.add(table.getRecordList().get(i));
                i++;
            }
        }
        this.dbmsController.getDatabaseController().getHelper().requestClone();
    }

    private boolean evaluate(String expression, Record record) {
        String exp = getFilledExpression(expression, record);
        exp = exp.toLowerCase();
        exp = App.replace(exp, "and", " && ");
        exp = App.replace(exp, "or", " || ");
        exp = App.replace(exp, "not", " ! ");
        exp = App.replace(exp, "=", "==");
        exp = App.replace(exp, ">==", ">=");
        exp = App.replace(exp, "<==", "<=");
        exp = App.replace(exp, "====", "==");

        try {
            return BooleanEvaluator.evaluate(exp);
        } catch (ScriptException e) {
            throw new RuntimeException("Invalid condition!");
        }
    }

    private String getFilledExpression(String expression, Record record) {
        String exp = expression.toLowerCase();
        int i = 0;
        for (String column : record.getColumns().keySet()) {
            if (record.getColumns().get(column).equals(String.class)) {
                exp = App.replace(exp, column.toLowerCase(), record.getValues().get(i).toString());
            } else {
                exp = App.replace(exp, column.toLowerCase(), record.getValues().get(i).toString());
            }
            i++;
        }
        return exp;
    }

    @Override
    public void order(Map<String, String> columns) throws RuntimeException {
        Map<String, Integer> columnIndex = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        SelectionTable table = this.dbmsController.getDatabaseController().getHelper()
                .getSelectedTable();
        int i = 0;
        for (String column : table.getHeader().keySet()) {
            columnIndex.put(column, i++);
        }
        Collections.sort(this.dbmsController.getDatabaseController().getHelper().getSelectedTable()
                .getRecordList(), new Comparator<Record>() {

                    @Override
                    public int compare(Record r1, Record r2) {
                        CompareToBuilder compare = new CompareToBuilder();
                        for (String column : columns.keySet()) {
                            int index = columnIndex.get(column);
                            Class<?> type = table.getDefaultHeader().get(column.toLowerCase());
                            if (columns.get(column).equals("ASC"))
                                compare.append(
                                        TypeFactory.parseToObject(type,
                                                String.valueOf(r1.getValues().get(index))),
                                        TypeFactory.parseToObject(type,
                                                String.valueOf(r2.getValues().get(index))));
                            else
                                compare.append(
                                        TypeFactory.parseToObject(type,
                                                String.valueOf(r2.getValues().get(index))),
                                        TypeFactory.parseToObject(type,
                                                String.valueOf(r1.getValues().get(index))));
                        }
                        return compare.toComparison();
                    }
                });
        this.dbmsController.getDatabaseController().getHelper().requestClone();
    }

    @Override
    public void whereForDelete(String condition) throws RuntimeException {
        if (!(this.dbmsController.getSQLParserController().getSqlParserHelper()
                .getCurrentQuery() instanceof Delete))
            return;
        SelectionTable originalTable = this.dbmsController.getDatabaseController().getHelper()
                .getTempTable();
        for (int i = 0; i < originalTable.getRecordList().size(); i++) {
            if (!this.evaluate(condition, originalTable.getRecordList().get(i))) {
                this.dbmsController.getDatabaseController().getHelper().getSelectedTable()
                        .getRecordList().add(originalTable.getRecordList().get(i));
                this.dbmsController.getDatabaseController().getHelper().getSelectedTable()
                        .decrementAffectedRecords();
            }
        }
        this.dbmsController.getDatabaseController().getHelper().requestClone();
    }

    @Override
    public void whereForSelect(String condition) throws RuntimeException {
        if (!(this.dbmsController.getSQLParserController().getSqlParserHelper()
                .getCurrentQuery() instanceof Select))
            return;
        SelectionTable originalTable = this.dbmsController.getDatabaseController().getHelper()
                .getTempTable();
        for (int i = originalTable.getRecordList().size() - 1; i >= 0; i--) {
            if (!this.evaluate(condition, originalTable.getRecordList().get(i))) {
                this.dbmsController.getDatabaseController().getHelper().getSelectedTable()
                        .getRecordList().remove(i);
            }
        }
        this.dbmsController.getDatabaseController().getHelper().requestClone();
    }

    @Override
    public void whereForUpdate(String condition) throws RuntimeException {
        if (!(this.dbmsController.getSQLParserController().getSqlParserHelper()
                .getCurrentQuery() instanceof Update))
            return;
        SelectionTable originalTable = this.dbmsController.getDatabaseController().getHelper()
                .getTempTable();
        for (int i = 0; i < originalTable.getRecordList().size(); i++) {
            if (!this.evaluate(condition, originalTable.getRecordList().get(i))) {
                this.dbmsController.getDatabaseController().getHelper().getSelectedTable()
                        .getRecordList().set(i, originalTable.getRecordList().get(i));
                this.dbmsController.getDatabaseController().getHelper().getSelectedTable()
                        .decrementAffectedRecords();
            }
        }
        this.dbmsController.getDatabaseController().getHelper().requestClone();
    }

}
