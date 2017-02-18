package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ShowDonationAlert;

/**
 * Created by Константин on 14.02.2017.
 */
public class EnteringAreaInterceptor extends MessageInterceptor {
    private int callCount = 0;
    @Override
    protected void process(String message) {
        if(callCount % 10 == 0) {
            EventRouter.INSTANCE.fireEvent(new ShowDonationAlert());
        }
        callCount++;
    }

    @Override
    protected MessageFilter getFilter() {
        return new MessageFilter() {
            @Override
            public boolean isMatching(String message) {
                return message.contains("Got Instance Details");
            }
        };
    }
}
