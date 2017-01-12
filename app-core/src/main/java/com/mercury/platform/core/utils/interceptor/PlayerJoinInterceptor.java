package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.PlayerJoinEvent;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Константин on 11.01.2017.
 */
public class PlayerJoinInterceptor extends MessageInterceptor {
    @Override
    protected void process(String message) {
        EventRouter.fireEvent(new PlayerJoinEvent(StringUtils.substringBetween(message," : ", " has joined the area.")));
    }

    @Override
    protected MessageFilter getFilter() {
        return new MessageFilter() {
            @Override
            public boolean isMatching(String message) {
                return message.contains("has joined the area.");
            }
        };
    }
}
