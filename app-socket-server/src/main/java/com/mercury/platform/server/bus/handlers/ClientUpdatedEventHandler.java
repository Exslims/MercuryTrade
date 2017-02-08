package com.mercury.platform.server.bus.handlers;

import com.google.common.eventbus.Subscribe;
import com.mercury.platform.server.bus.event.ClientUpdatedEvent;

/**
 * Created by Frost on 08.02.2017.
 */
public interface ClientUpdatedEventHandler {
    @Subscribe void onClientUpdated(ClientUpdatedEvent event);
}
