package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.OutTradeMessageEvent;
import com.mercury.platform.shared.pojo.Message;

/**
 * Created by Константин on 12.01.2017.
 */
public class OutTradeMessagesInterceptor extends MessageInterceptor {
    private MessageParser messageParser = new MessageParser();
    @Override
    protected void process(String message) {
        Message parsedMessage = messageParser.parse(message);
        EventRouter.CORE.fireEvent(new OutTradeMessageEvent(parsedMessage));
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
