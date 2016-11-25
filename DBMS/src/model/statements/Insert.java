package model.statements;

public class Insert implements Query {
    private String identidier;

    public Insert(String identifier) {
        this.identidier = identifier;
    }
}
