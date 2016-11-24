package model.statements;

public class Delete implements Query {
    private String identidier;

    public Delete(String identifier) {
        this.identidier = identifier;
    }
}
