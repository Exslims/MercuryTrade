package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.utils.interceptor.filter.MessageMatcher;

/**
 * Created by Константин on 11.01.2017.
 */
public abstract class MessageInterceptor {
    protected MessageMatcher filter;

    public MessageInterceptor() {
        filter = match();
    }

    public boolean match(String message) {
        if (filter.isMatching(message)) {
            process(message);
            return true;
        }
        return false;
    }

    protected abstract void process(String message);

    protected abstract MessageMatcher match();
}
