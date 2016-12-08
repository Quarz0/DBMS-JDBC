package model.statements;

import java.util.ArrayList;
import java.util.List;

public abstract class Query implements ExecutableQuery, Parsable {

    protected List<Clause> clauses;

    protected Query() {
        clauses = new ArrayList<>();
    }

    public void addClause(Clause clause) {
        this.clauses.add(clause);
    }

    public List<Clause> getClauses() {
        return clauses;
    }
    
}
