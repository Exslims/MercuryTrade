package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;

/**
 * Created by Константин on 11.01.2017.
 */
public abstract class MessageInterceptor {
    protected MessageFilter filter;

    public MessageInterceptor() {
        filter = getFilter();
    }

    public boolean match(String message) {
        if (filter.isMatching(message)) {
            process(message);
            return true;
        }
        return false;
    }

    protected abstract void process(String message);

    protected abstract MessageFilter getFilter();
}
