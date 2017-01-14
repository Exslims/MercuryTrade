package com.mercury.platform.queue.message;

import java.io.Serializable;

/**
 * Created by Frost on 14.01.2017.
 */
public interface Message<T> extends Comparable<Message> {
    T getValue();
}
