package model.statements;

import java.util.ArrayList;
import java.util.List;

public abstract class Query implements Executable, Parsable {

    protected List<Clause> clauses;

    protected Query() {
        clauses = new ArrayList<>();
    }

    public void addClauses(Clause clause) {
        this.clauses.add(clause);
    }
}
