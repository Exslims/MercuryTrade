package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.utils.interceptor.filter.MessageMatcher;
import com.mercury.platform.shared.entity.message.PlainMessageDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.commons.lang3.StringUtils;


public class PlayerInaccessibleInterceptor extends MessageInterceptor {
    private PlainMessageDescriptor lastPlainMessage;

    public PlayerInaccessibleInterceptor() {
        MercuryStoreCore.plainMessageSubject.subscribe(message -> {
            this.lastPlainMessage = message;
        });
    }

    @Override
    protected void process(String message) {
        this.lastPlainMessage.setMessage(StringUtils.substringAfter(message, " : "));
        MercuryStoreCore.plainMessageSubject.onNext(this.lastPlainMessage);
    }

    @Override
    protected MessageMatcher match() {
        return message -> message.contains("That character is not online.");
    }
}
