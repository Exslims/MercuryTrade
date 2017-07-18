package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.commons.lang3.StringUtils;

public class TradeMessagesInterceptor extends MessageInterceptor {
    private MessageParser messageParser = new MessageParser();

    @Override
    protected void process(String message) {
        Message parsedMessage = messageParser.parse(StringUtils.substringAfter(message,"@"));
        if(parsedMessage != null) {
            MercuryStoreCore.soundSubject.onNext(SoundType.MESSAGE);
            MercuryStoreCore.messageSubject.onNext(parsedMessage);
        }
    }

    @Override
    protected MessageFilter getFilter() {
        return message -> message.contains("@From") && (message.contains("Hi, I would like") || message.contains("Hi, I'd like"));
    }
}
