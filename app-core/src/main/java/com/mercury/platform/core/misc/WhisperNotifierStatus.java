package com.mercury.platform.core.misc;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Константин on 13.12.2016.
 */
public enum WhisperNotifierStatus {
    ALWAYS(0),
    ALTAB(1),
    NONE(2);

    private static final Map<Integer, WhisperNotifierStatus> lookup
            = new HashMap<>();
    static {
        for (WhisperNotifierStatus w : EnumSet.allOf(WhisperNotifierStatus.class)){
            lookup.put(w.getCode(),w);
        }
    }
    private int code;
    private WhisperNotifierStatus(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    public static WhisperNotifierStatus get(int code){
        return lookup.get(code);
    }
}
