package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.NewWhispersEvent;
import com.mercury.platform.shared.events.custom.SoundNotificationEvent;
import com.mercury.platform.shared.pojo.Message;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Константин on 11.01.2017.
 */
public class IncTradeMessagesInterceptor extends MessageInterceptor {
    private MessageParser messageParser = new MessageParser();

    @Override
    protected void process(String message) {
        Message parsedMessage = messageParser.parse(StringUtils.substringAfter(message,"@"));
        if(parsedMessage != null) {
            EventRouter.CORE.fireEvent(new SoundNotificationEvent.WhisperSoundNotificationEvent());
            EventRouter.CORE.fireEvent(new NewWhispersEvent(parsedMessage));
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
