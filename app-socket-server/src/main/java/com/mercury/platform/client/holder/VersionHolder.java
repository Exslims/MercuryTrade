package com.mercury.platform.client.holder;

import com.mercury.platform.holder.UpdateHolder;

/**
 * Created by Frost on 28.01.2017.
 */
public class VersionHolder {

    private static volatile VersionHolder instance;

    public static VersionHolder getInstance() {
        VersionHolder localInstance = instance;
        if (localInstance == null) {
            synchronized (VersionHolder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new VersionHolder();
                }
            }
        }
        return localInstance;
    }

    private volatile int version;

    private VersionHolder() {
    }


    public int getVersion() {
        return version;
    }

    public synchronized void setVersion(int version) {
        this.version = version;
    }
}
