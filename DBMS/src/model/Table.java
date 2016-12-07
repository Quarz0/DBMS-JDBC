package model;

import java.io.File;

public class Table {
    private File dtdFile;
    private File tableDir;
    private File xmlFile;

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

    public File getDTD() {
        if (!dtdFile.exists()) {
            return null;
        }
        return dtdFile;
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

    public File getXML() {
        if (!xmlFile.exists()) {
            return null;
        }
        return xmlFile;
    }

    public void registerFiles(File xmlFile, File dtdFile) {
        this.xmlFile = xmlFile;
        this.dtdFile = dtdFile;
    }

}
