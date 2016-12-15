package eg.edu.alexu.csd.oop.DBMS.model.statements.queries;

import java.text.ParseException;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMS;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Clause;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Query;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Writable;
import eg.edu.alexu.csd.oop.DBMS.model.statements.clauses.Where;
import eg.edu.alexu.csd.oop.DBMS.util.App;
import eg.edu.alexu.csd.oop.DBMS.util.Regex;
import eg.edu.alexu.csd.oop.DBMS.util.RegexEvaluator;

public class Delete extends Query implements Writable {

    private boolean isAll;
    private String tableIdentifier;

    public Delete() {
        super();
        this.isAll = false;
    }

    @Override
    public void addClause(Clause clause) {
        super.addClause(clause);
        if (clause instanceof Where && this.isAll) {
            throw new RuntimeException("Hell! What exactly should I delete?!");
        }
    }

    private boolean checkRegex(String s) {
        String[] groups = RegexEvaluator.evaluate(s, Regex.PARSE_WITH_DELETE_ALL);
        if (App.checkForExistence(groups)) {
            this.isAll = App.checkForExistence(groups[1]);
            this.extractTable(groups[2].trim());
            return true;
        }
        return false;
    }

    @Override
    public void execute(DBMS dbms) throws RuntimeException {
        dbms.deleteFromTable(this.getTableIdentifier());
    }

    private void extractTable(String s) {
        this.tableIdentifier = s.trim();
    }

    public String getTableIdentifier() {
        return this.tableIdentifier;
    }

    public boolean isAll() {
        return this.isAll;
    }

    @Override
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            throw new ParseException("Syntax error!(not a valid delete statement)", 0);
    }

}
