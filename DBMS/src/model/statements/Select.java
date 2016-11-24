package model.statements;

public class Select implements Query {
    private String identidier;

    public Select(String identifier) {
        this.identidier = identifier;
    }
}
