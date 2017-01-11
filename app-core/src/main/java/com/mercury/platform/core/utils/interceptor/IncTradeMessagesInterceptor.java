package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.NewWhispersEvent;
import com.mercury.platform.shared.events.custom.WhisperNotificationEvent;
import com.mercury.platform.shared.pojo.Message;

/**
 * Created by Константин on 11.01.2017.
 */
public class IncTradeMessagesInterceptor extends MessageInterceptor {
    private MessageParser messageParser = new MessageParser();
    @Override
    public void match(String messageSting) {
        if(filter.isMatching(messageSting)){
            Message message = messageParser.parse(messageSting);
            EventRouter.fireEvent(new WhisperNotificationEvent());
            EventRouter.fireEvent(new NewWhispersEvent(message));
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
