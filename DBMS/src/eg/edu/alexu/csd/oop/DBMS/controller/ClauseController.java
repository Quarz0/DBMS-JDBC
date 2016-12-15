package eg.edu.alexu.csd.oop.DBMS.controller;

import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.script.ScriptException;

import org.apache.commons.lang3.builder.CompareToBuilder;

import eg.edu.alexu.csd.oop.DBMS.model.Record;
import eg.edu.alexu.csd.oop.DBMS.model.SelectionTable;
import eg.edu.alexu.csd.oop.DBMS.model.statements.queries.Delete;
import eg.edu.alexu.csd.oop.DBMS.model.statements.queries.Select;
import eg.edu.alexu.csd.oop.DBMS.model.statements.queries.Update;
import eg.edu.alexu.csd.oop.DBMS.util.App;
import eg.edu.alexu.csd.oop.DBMS.util.BooleanEvaluator;

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
        for (String column : record.getColumns().keySet()) {// any quoted type must be set in this
                                                            // if checking[To be changed]!
            if (record.getColumns().get(column).equals(String.class)
                    || record.getColumns().get(column).equals(Date.class)) {
                if (App.checkForExistence(record.getValues().get(i))) {
                    exp = App.replace(exp, column.toLowerCase(),
                            "'" + record.getValues().get(i).toString() + "'");
                } else {
                    exp = App.replace(exp, column.toLowerCase(), "null");
                }
            } else {
                exp = App.replace(exp, column.toLowerCase(),
                        App.checkForExistence(record.getValues().get(i))
                                ? record.getValues().get(i).toString() : "NULL");
            }
            i++;
        }
        return exp;
    }

    @Override
    public void order(Map<String, String> columns) throws RuntimeException {
        Map<String, Integer> columnIndex = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        SelectionTable tempTable = this.dbmsController.getDatabaseController().getHelper()
                .getTempTable();

        int i = 0;
        for (String column : tempTable.getHeader().keySet()) {
            columnIndex.put(column, i++);
        }
        Collections.sort(this.dbmsController.getDatabaseController().getHelper().getTempTable()
                .getRecordList(), new Comparator<Record>() {

                    @Override
                    public int compare(Record r1, Record r2) {
                        CompareToBuilder compare = new CompareToBuilder();
                        for (String column : columns.keySet()) {
                            int index = columnIndex.get(column);
                            Class<?> type = tempTable.getDefaultHeader().get(column.toLowerCase());
                            if (columns.get(column).equals("ASC"))
                                compare.append(type.cast(r1.getValues().get(index)),
                                        type.cast(r2.getValues().get(index)));
                            else
                                compare.append(type.cast(r2.getValues().get(index)),
                                        type.cast(r1.getValues().get(index)));
                        }
                        return compare.toComparison();
                    }
                });
        SelectionTable table = this.dbmsController.getDatabaseController().getHelper()
                .getSelectedTable();
        for (i = 0; i < table.getRecordList().size(); i++) {
            int j = 0;
            for (String column : table.getRecordList().get(i).getColumns().keySet()) {
                table.getRecordList().get(i).getValues().set(j,
                        tempTable.getRecordList().get(i).getValues().get(columnIndex.get(column)));
                j++;
            }
        }
        this.dbmsController.getDatabaseController().getHelper().requestClone();
    }

    @Override
    public void union(Select select) throws RuntimeException {
        SelectionTable defaultTable = this.dbmsController.getDatabaseController().getHelper()
                .getSelectedTable();
        SelectionTable unionTable = this.dbmsController.getDatabaseController().getHelper()
                .getUnionTable();

        if (!App.checkForExistence(unionTable)) {
            try {
                unionTable = (SelectionTable) defaultTable.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Run, Forest, Run!");
            }
        }
        select.execute(this.dbmsController.getDatabaseController());
        select.getClauses().forEach(clause -> clause.execute(this));
        defaultTable = this.dbmsController.getDatabaseController().getHelper().getSelectedTable();
        if (!defaultTable.isTypeEqual(unionTable))
            throw new RuntimeException("Inconsistent data!");

        try {
            for (Record record : defaultTable.getRecordList()) {
                unionTable.addRecord((Record) record.clone());
            }
            this.dbmsController.getDatabaseController().getHelper()
                    .setSelectedTable((SelectionTable) unionTable.clone());
            this.distinct();
            unionTable = (SelectionTable) this.dbmsController.getDatabaseController().getHelper()
                    .getSelectedTable().clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Run, Forest, Run!");
        }

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
