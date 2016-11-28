package model;

import java.io.File;
import java.io.FileFilter;

public class DatabaseFilterGenerator implements FileFilter {
    @Override
    public boolean accept(File file) {
        return file.isDirectory();
    }
}
