package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.entity.Message;
import com.mercury.platform.shared.store.MercuryStore;
import org.apache.commons.lang3.StringUtils;

public class IncTradeMessagesInterceptor extends MessageInterceptor {
    private MessageParser messageParser = new MessageParser();

    @Override
    protected void process(String message) {
        Message parsedMessage = messageParser.parse(StringUtils.substringAfter(message,"@"));
        if(parsedMessage != null) {
            MercuryStore.INSTANCE.soundSubject.onNext(SoundType.MESSAGE);
            MercuryStore.INSTANCE.messageSubject.onNext(parsedMessage);
        }
    }

    @Override
    protected MessageFilter getFilter() {
        return new MessageFilter() {
            @Override
            public boolean isMatching(String message) {
                return message.contains("@From") && (message.contains("Hi, I would like") || message.contains("Hi, I'd like"));
            }
        };
    }
}
