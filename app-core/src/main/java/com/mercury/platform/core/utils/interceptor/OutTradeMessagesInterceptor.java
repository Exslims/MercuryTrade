package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.shared.store.MercuryStoreCore;


public class OutTradeMessagesInterceptor extends MessageInterceptor {
    private MessageParser messageParser = new MessageParser();
    @Override
    protected void process(String message) {
        Message parsedMessage = messageParser.parse(message);
        MercuryStoreCore.INSTANCE.outMessageSubject.onNext(parsedMessage);
    }

    @Override
    protected MessageFilter getFilter() {
        return new MessageFilter() {
            @Override
            public boolean isMatching(String message) {
                return message.contains("@To") && (message.contains("Hi, I would like") || message.contains("Hi, I'd like"));
            }
        };
    }
}
