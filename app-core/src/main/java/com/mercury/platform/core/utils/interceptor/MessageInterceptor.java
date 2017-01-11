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

    public abstract void match(String message);
    protected abstract MessageFilter getFilter();
}
