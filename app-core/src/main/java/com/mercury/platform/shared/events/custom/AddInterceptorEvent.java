package com.mercury.platform.shared.events.custom;

import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.shared.events.MercuryEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddInterceptorEvent implements MercuryEvent {
    private MessageInterceptor interceptor;
}
