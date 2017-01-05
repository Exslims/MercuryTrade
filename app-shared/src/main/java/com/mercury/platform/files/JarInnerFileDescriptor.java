package com.mercury.platform.files;

/**
 * Created by Константин on 05.01.2017.
 */
public class JarInnerFileDescriptor extends FileDescriptor {
    private FileStatus status;

    public JarInnerFileDescriptor(FileStatus status, String path, byte[] fileAsBytes) {
        super(path,fileAsBytes);
        this.status = status;
    }

    public FileStatus getStatus() {
        return status;
    }

    public void setStatus(FileStatus status) {
        this.status = status;
    }
}
