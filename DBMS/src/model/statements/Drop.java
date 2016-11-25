package model.statements;

public class Drop implements Query {
    private String identidier;
    private Class<?> type;

    public Drop(Class<?> type, String identifier) {
        this.type = type;
        this.identidier = identifier;
    }
}
