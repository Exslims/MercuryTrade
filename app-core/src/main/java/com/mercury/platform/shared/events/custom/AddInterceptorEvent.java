package com.mercury.platform.shared.events.custom;

import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 11.01.2017.
 */
public class AddInterceptorEvent implements MercuryEvent {
    private MessageInterceptor interceptor;

    public AddInterceptorEvent(MessageInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public MessageInterceptor getInterceptor() {
        return interceptor;
    }
}
