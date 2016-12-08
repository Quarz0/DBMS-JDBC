package model;

import java.io.File;

import controller.BackEndWriter;

public class Table {
    private File validatorFile;
    private File tableDir;
    private File dataFile;
    private BackEndWriter backEndWriter;

    public Table(File tableDir, BackEndWriter backEndWriter) {
        this.tableDir = tableDir;
        this.backEndWriter = backEndWriter;
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

    public File getValidator() {
        if (!validatorFile.exists()) {
            return null;
        }
        return validatorFile;
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

    public File getData() {
        if (!dataFile.exists()) {
            return null;
        }
        return dataFile;
    }

    public void registerFiles(File dataFile, File validatorFile) {
        this.dataFile = dataFile;
        this.validatorFile = validatorFile;
    }

    public BackEndWriter getBackEndWriter() {
        return backEndWriter;
    }

}
