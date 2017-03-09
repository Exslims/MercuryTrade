package com.mercury.platform.update;

import java.io.Serializable;

/**
 * Created by Константин on 07.03.2017.
 */
public class UpdateDescriptor implements Serializable {
    private UpdateType type;
    private int version;

    public UpdateDescriptor(UpdateType type, int version) {
        this.type = type;
        this.version = version;
    }

    public UpdateType getType() {
        return type;
    }

    public void setType(UpdateType type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
