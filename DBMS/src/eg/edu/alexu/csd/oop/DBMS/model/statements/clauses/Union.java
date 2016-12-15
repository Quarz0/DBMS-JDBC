package eg.edu.alexu.csd.oop.DBMS.model.statements.clauses;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import eg.edu.alexu.csd.oop.DBMS.controller.DBMSClause;
import eg.edu.alexu.csd.oop.DBMS.model.statements.Clause;
import eg.edu.alexu.csd.oop.DBMS.model.statements.queries.Select;
import eg.edu.alexu.csd.oop.DBMS.util.App;

public class Union extends Clause {

    private List<Select> selects;

    public Union() {
        super();
        this.selects = new ArrayList<>();
    }

    private boolean checkRegex(String s) throws ParseException {
        String[] groups = s.trim().split("(?i)UNION");
        if (App.checkForExistence(groups)) {
            return this.extractSelectStatements(groups);
        }
        return false;
    }

    @Override
    public void execute(DBMSClause dbms) throws RuntimeException {
        for (Select select : this.selects)
            dbms.union(select);
    }

    private boolean extractSelectStatements(String[] groups) throws ParseException {
        for (int i = 0; i < groups.length; i++) {
            String[] whereS = groups[i].trim().split("(?i)WHERE");
            if (whereS.length > 2)
                return false;
            Select select = new Select();
            select.parse(whereS[0].trim().substring(7).trim());
            if (whereS.length > 1) {
                Where where = new Where();
                where.parse(whereS[1].trim());
                select.addClause(where);
            }
            this.selects.add(select);
        }
        return true;
    }

    @Override
    public void parse(String s) throws ParseException {
        if (!App.checkForExistence(s) || !this.checkRegex(s))
            throw new ParseException("Syntax error!(not a valid union clause)", 0);
    }

}
