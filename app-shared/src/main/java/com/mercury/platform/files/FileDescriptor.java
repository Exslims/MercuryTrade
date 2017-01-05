package com.mercury.platform.files;

import java.io.Serializable;

/**
 * Created by Константин on 05.01.2017.
 */
public class FileDescriptor implements Serializable{
    private String path;
    private byte[] fileAsBytes;

    public FileDescriptor(String path, byte[] fileAsBytes) {
        this.path = path;
        this.fileAsBytes = fileAsBytes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getFileAsBytes() {
        return fileAsBytes;
    }

    public void setFileAsBytes(byte[] fileAsBytes) {
        this.fileAsBytes = fileAsBytes;
    }
}
