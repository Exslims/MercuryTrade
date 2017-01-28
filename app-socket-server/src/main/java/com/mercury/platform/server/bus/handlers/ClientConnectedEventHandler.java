package com.mercury.platform.server.bus.handlers;

import com.google.common.eventbus.Subscribe;
import com.mercury.platform.server.bus.event.ClientConnectedEvent;

/**
 * Created by Frost on 25.01.2017.
 */
public interface ClientConnectedEventHandler {
    @Subscribe void onClientConnected(ClientConnectedEvent event);
}
