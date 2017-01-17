package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.PlayerLeftEvent;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Константин on 11.01.2017.
 */
public class PlayerLeftInterceptor extends MessageInterceptor {
    @Override
    protected void process(String message) {
        EventRouter.INSTANCE.fireEvent(new PlayerLeftEvent(StringUtils.substringBetween(message," : ", " has left the area.")));
    }

    @Override
    protected MessageFilter getFilter() {
        return new MessageFilter() {
            @Override
            public boolean isMatching(String message) {
                return message.contains("has left the area.");
            }
        };
    }
}
