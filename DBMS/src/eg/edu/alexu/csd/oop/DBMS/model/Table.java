package eg.edu.alexu.csd.oop.DBMS.model;

import java.io.File;

public class Table {
    private File dataFile;
    private File tableDir;
    private File validatorFile;

    public Table(File tableDir) {
        this.tableDir = tableDir;
        this.createDir();
    }

    private void createDir() {
        if (tableDir.exists()) {
            return;
        }
        if (!tableDir.mkdir()) {
            throw new RuntimeException("Cannot create table directory");
        }
    }

    public File getData() {
        if (!dataFile.exists()) {
            return null;
        }
        return dataFile;
    }

    public File getTableDir() {
        return tableDir;

    }

    public String getTableName() {
        return tableDir.getName();
    }

    public String getTablePath() {
        return tableDir.getAbsolutePath();
    }

    public File getValidator() {
        if (!validatorFile.exists()) {
            return null;
        }
        return validatorFile;
    }

    public void registerFiles(File dataFile, File validatorFile) {
        this.dataFile = dataFile;
        this.validatorFile = validatorFile;
    }

}
