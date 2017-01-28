package com.mercury.platform.server.bus.handlers;

import com.google.common.eventbus.Subscribe;
import com.mercury.platform.server.bus.event.ClientActiveEvent;

/**
 * Created by Frost on 25.01.2017.
 */
public interface ClientActiveEventHandler {
    @Subscribe void onClientConnected(ClientActiveEvent event);
}
