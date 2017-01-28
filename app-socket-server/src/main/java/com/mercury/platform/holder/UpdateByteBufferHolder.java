package com.mercury.platform.holder;

import java.nio.ByteBuffer;

/**
 * Created by Frost on 27.01.2017.
 */
public class UpdateByteBufferHolder {

    private static volatile UpdateByteBufferHolder instance;

    public static UpdateByteBufferHolder getInstance() {
        UpdateByteBufferHolder localInstance = instance;
        if (localInstance == null) {
            synchronized (UpdateByteBufferHolder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UpdateByteBufferHolder();
                }
            }
        }
        return localInstance;
    }

    private volatile ByteBuffer update;

    private UpdateByteBufferHolder() {
    }

    public synchronized void setUpdate(ByteBuffer update) {
        this.update = update;
    }

    public ByteBuffer getUpdate() {
        return update;
    }
}
