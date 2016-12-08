package controller;

import java.util.List;
import java.util.Map;

public interface DBMSClause {

    public void whereForDelete(String condition) throws RuntimeException;
    
    public void whereForSelect(String condition) throws RuntimeException;
    
    public void whereForUpdate(String condition) throws RuntimeException;

    public void order(Map<String, String> columns) throws RuntimeException;
    
    public void distinct(List<String> columns) throws RuntimeException;
}
