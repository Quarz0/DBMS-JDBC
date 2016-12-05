package controller;

public class ClauseController implements DBMSClause {

    private DBMSController dbmsController;

    public ClauseController(DBMSController dbmsController) {
        this.dbmsController = dbmsController;
    }

    @Override
    public void whereCondition(String condition) throws RuntimeException {
        // TODO Auto-generated method stub

    }

}
