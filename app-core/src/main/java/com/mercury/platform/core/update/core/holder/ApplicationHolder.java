package com.mercury.platform.core.update.core.holder;

/**
 * Created by Frost on 28.01.2017.
 */
public class ApplicationHolder {

    private static volatile ApplicationHolder instance;
    private volatile int version;
    private volatile boolean manualRequest;

    private ApplicationHolder() {
        manualRequest = false;
    }

    public static ApplicationHolder getInstance() {
        ApplicationHolder localInstance = instance;
        if (localInstance == null) {
            synchronized (ApplicationHolder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ApplicationHolder();
                }
            }
        }
        return localInstance;
    }

    public int getVersion() {
        return version;
    }

    public synchronized void setVersion(int version) {
        this.version = version;
    }

    public boolean isManualRequest() {
        return manualRequest;
    }

    public synchronized void setManualRequest(boolean manualRequest) {
        this.manualRequest = manualRequest;
    }
}
