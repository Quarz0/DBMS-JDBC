package eg.edu.alexu.csd.oop.DBMS.model.statements;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import eg.edu.alexu.csd.oop.DBMS.model.statements.clauses.Distinct;
import eg.edu.alexu.csd.oop.DBMS.model.statements.clauses.Order;
import eg.edu.alexu.csd.oop.DBMS.model.statements.clauses.Union;
import eg.edu.alexu.csd.oop.DBMS.model.statements.clauses.Where;

public abstract class Query implements ExecutableQuery, Parsable {

    protected Queue<Clause> clauses;

    protected Query() {
        clauses = new PriorityQueue<>(new Comparator<Clause>() {
            @Override
            public int compare(Clause o1, Clause o2) {
                if (o1 instanceof Where)
                    return -1;
                else if (o2 instanceof Where)
                    return 1;
                if (o1 instanceof Union)
                    return -1;
                else if (o2 instanceof Union)
                    return 1;
                if (o1 instanceof Distinct)
                    return -1;
                else if (o2 instanceof Distinct)
                    return 1;
                if (o1 instanceof Order)
                    return -1;
                else if (o2 instanceof Order)
                    return 1;
                return 0;
            }
        });
    }

    public void addClause(Clause clause) {
        this.clauses.add(clause);
    }

    public Queue<Clause> getClauses() {
        return clauses;
    }

}
