package model;

import java.io.File;

public class Table {
    private File tableDir;
    private File xmlFile;
    private File dtdFile;

    public Table(File tableDir) {
        this.tableDir = tableDir;
        createDir();
    }

    public String getTableName() {
        return tableDir.getName();
    }

    public File getXML() {
        if (!xmlFile.exists()) {
            return null;
        }
        return xmlFile;
    }

    public File getDTD() {
        if (!dtdFile.exists()) {
            return null;
        }
        return dtdFile;
    }

    public String getTablePath() {
        return tableDir.getAbsolutePath();
    }

    public File getTableDir() {
        return tableDir;

    }

    private void createDir() {
        if (tableDir.exists()) {
            return;
        }
        if (!tableDir.mkdir()) {
            throw new RuntimeException("Cannot create table directory");
        }
    }

    public void registerFiles(File xmlFile, File dtdFile) {
        this.xmlFile = xmlFile;
        this.dtdFile = dtdFile;
    }

}
