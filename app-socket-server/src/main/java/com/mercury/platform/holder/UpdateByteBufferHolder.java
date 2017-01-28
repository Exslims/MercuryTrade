package com.mercury.platform.holder;

import java.nio.ByteBuffer;

/**
 * Created by Frost on 27.01.2017.
 */
public class UpdateByteBufferHolder {
    private static UpdateByteBufferHolder instance = new UpdateByteBufferHolder();

    public static UpdateByteBufferHolder getInstance() {
        return instance;
    }

    private ByteBuffer update;

    private UpdateByteBufferHolder() {
    }

    public synchronized void setUpdate(ByteBuffer update) {
        this.update = update;
    }

    public ByteBuffer getUpdate() {
        return update;
    }
}
