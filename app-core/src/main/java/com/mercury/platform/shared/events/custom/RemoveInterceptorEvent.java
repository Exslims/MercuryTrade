package com.mercury.platform.shared.events.custom;

import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.shared.events.MercuryEvent;

public class RemoveInterceptorEvent implements MercuryEvent {
    private MessageInterceptor interceptor;

    public RemoveInterceptorEvent(MessageInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public MessageInterceptor getInterceptor() {
        return interceptor;
    }
}
