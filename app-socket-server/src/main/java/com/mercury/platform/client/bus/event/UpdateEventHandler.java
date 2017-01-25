package com.mercury.platform.client.bus.event;

import com.google.common.eventbus.Subscribe;

/**
 * Created by Frost on 25.01.2017.
 */
public interface UpdateEventHandler {
    @Subscribe void onUpdateReceived(UpdateReceivedEvent jarFile);
}
