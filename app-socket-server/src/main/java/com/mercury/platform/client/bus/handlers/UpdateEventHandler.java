package com.mercury.platform.client.bus.handlers;

import com.google.common.eventbus.Subscribe;
import com.mercury.platform.client.bus.event.UpdateReceivedEvent;

/**
 * Created by Frost on 25.01.2017.
 */
public interface UpdateEventHandler {
    @Subscribe void onUpdateReceived(UpdateReceivedEvent jarFile);
}
