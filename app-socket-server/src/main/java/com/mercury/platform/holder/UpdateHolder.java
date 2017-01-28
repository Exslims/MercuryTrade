package com.mercury.platform.holder;

/**
 * Created by Frost on 27.01.2017.
 */
public class UpdateHolder {

    private static volatile UpdateHolder instance;

    public static UpdateHolder getInstance() {
        UpdateHolder localInstance = instance;
        if (localInstance == null) {
            synchronized (UpdateHolder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UpdateHolder();
                }
            }
        }
        return localInstance;
    }

    private volatile byte[] update;
    private volatile int version;

    private UpdateHolder() {
    }

    public synchronized void setUpdate(byte[] update) {
        this.update = update;
    }

    public byte[] getUpdate() {
        return update;
    }


    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
