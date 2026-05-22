package org.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileDataSource implements MissionDataSource {
    private final File file;

    public FileDataSource(File file) {
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws Exception {
        return new FileInputStream(file);
    }

    @Override
    public String getIdentifier() {
        return file == null ? "" : file.getName().toLowerCase();
    }

    @Override
    public boolean isValid() {
        return file != null && file.exists() && file.isFile();
    }

    public File getFile() {
        return file;
    }
}
