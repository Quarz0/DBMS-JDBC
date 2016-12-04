package model.statements;

public enum Operator {
    EQUAL(1), GREATER_OR_EQUAL(5), GREATER_THAN(3), LESS_OR_EQUAL(6), LESS_THAN(4), NONE(
            0), NOT_EQUAL(2);

    public static Operator fromInteger(int op) {
        switch (op) {
        case 1:
            return EQUAL;
        case 2:
            return NOT_EQUAL;
        case 3:
            return GREATER_THAN;
        case 4:
            return LESS_THAN;
        case 5:
            return GREATER_OR_EQUAL;
        case 6:
            return LESS_OR_EQUAL;
        default:
            return NONE;
        }
    }

    public static Operator fromString(String op) {
        switch (op) {
        case "=":
            return EQUAL;
        case "!=":
            return NOT_EQUAL;
        case ">":
            return GREATER_THAN;
        case "<":
            return LESS_THAN;
        case ">=":
            return GREATER_OR_EQUAL;
        case "<=":
            return LESS_OR_EQUAL;
        default:
            return NONE;
        }
    }

    public static String toString(Operator operator) {
        switch (operator) {
        case EQUAL:
            return "=";
        case NOT_EQUAL:
            return "!=";
        case GREATER_THAN:
            return ">";
        case LESS_THAN:
            return "<";
        case GREATER_OR_EQUAL:
            return ">=";
        case LESS_OR_EQUAL:
            return "<=";
        default:
            break;
        }
        return null;
    }

    Operator(int strokeWidth) {
    }
}
