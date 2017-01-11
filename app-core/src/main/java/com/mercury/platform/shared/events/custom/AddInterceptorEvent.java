package com.mercury.platform.shared.events.custom;

import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.shared.events.SCEvent;

/**
 * Created by Константин on 11.01.2017.
 */
public class AddInterceptorEvent implements SCEvent {
    private MessageInterceptor interceptor;

    public AddInterceptorEvent(MessageInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public MessageInterceptor getInterceptor() {
        return interceptor;
    }
}
