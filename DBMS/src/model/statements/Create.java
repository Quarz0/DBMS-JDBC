package model.statements;

public class Create implements Query {
    private String identidier;
    private Class<?> type;

    public Create(Class<?> type, String identifier) {
        this.type = type;
        this.identidier = identifier;
    }
}
