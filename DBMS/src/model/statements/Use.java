package model.statements;

public class Use implements Query {
    private String identidier;

    public Use(String identifier) {
        this.identidier = identifier;
    }
}
