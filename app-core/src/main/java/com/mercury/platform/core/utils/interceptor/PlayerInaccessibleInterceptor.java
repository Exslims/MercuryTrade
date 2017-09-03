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
        if (lastPlainMessage != null) {
            PlainMessageDescriptor descriptor = new PlainMessageDescriptor();
            descriptor.setNickName(this.lastPlainMessage.getNickName());
            descriptor.setMessage(StringUtils.substringAfter(message, " : "));
            MercuryStoreCore.plainMessageSubject.onNext(descriptor);
        }
    }

    @Override
    protected MessageMatcher match() {
        return message -> true;
    }
}
