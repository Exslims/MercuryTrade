package com.mercury.platform.diff.entry;

/**
 * Created by Frost on 03.01.2017.
 */
public class JarEntryDescriptor {
    private String fileName;
    private long fileSize;
    private long creationTime;


    public JarEntryDescriptor(String fileName, long fileSize, long creationTime) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.creationTime = creationTime;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getCreationTime() {
        return creationTime;
    }
}
