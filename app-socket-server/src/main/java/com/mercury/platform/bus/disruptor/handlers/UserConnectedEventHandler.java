package com.mercury.platform.bus.disruptor.handlers;

import com.lmax.disruptor.EventHandler;
import com.mercury.platform.bus.disruptor.events.UserConnectedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Frost on 25.01.2017.
 */
public class UserConnectedEventHandler implements EventHandler<UserConnectedEvent> {

    private static final Logger LOGGER = LogManager.getLogger(UserConnectedEventHandler.class);

    @Override
    public void onEvent(UserConnectedEvent userConnectedEvent, long sequence, boolean endOfBatch) throws Exception {
        LOGGER.info("User connected. IP = " + userConnectedEvent.getIpAddress());
    }
}
