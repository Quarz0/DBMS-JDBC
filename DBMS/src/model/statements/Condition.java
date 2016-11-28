package model.statements;

public class Condition {

    private String leftOperand;
    private String rightOperand;
    private Operator operator;

    @Override
    public String toString() {
        return this.leftOperand + Operator.toString(this.operator) + this.rightOperand;
    }

}