package com.mercury.platform.bus.disruptor.events;

import com.lmax.disruptor.EventFactory;

/**
 * Created by Frost on 25.01.2017.
 */
public class UserConnectedEventFactory implements EventFactory<UserConnectedEvent> {
    @Override
    public UserConnectedEvent newInstance() {
        return new UserConnectedEvent();
    }
}
