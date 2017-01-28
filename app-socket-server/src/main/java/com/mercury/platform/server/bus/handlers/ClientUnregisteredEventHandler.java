package com.mercury.platform.server.bus.handlers;

import com.google.common.eventbus.Subscribe;
import com.mercury.platform.server.bus.event.ClientUnregisteredEvent;

/**
 * Created by Frost on 28.01.2017.
 */
public interface ClientUnregisteredEventHandler {
    @Subscribe
    void onClientDisconnected(ClientUnregisteredEvent event);
}
