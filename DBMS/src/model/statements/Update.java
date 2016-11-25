package model.statements;

public class Update implements Query {
    private String identidier;

    public Update(String identifier) {
        this.identidier = identifier;
    }
}
