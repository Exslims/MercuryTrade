package com.mercury.platform.core.update.bus;

import com.google.common.eventbus.EventBus;

/**
 * Created by Frost on 25.01.2017.
 */
public class UpdaterClientEventBus extends EventBus {

    private static final UpdaterClientEventBus instance = new UpdaterClientEventBus();
    private EventBus eventBus;

    private UpdaterClientEventBus() {
        this.eventBus = new EventBus();
    }

    public static UpdaterClientEventBus getInstance() {
        return instance;
    }

    @Override
    public void register(Object object) {
        eventBus.register(object);
    }

    @Override
    public void unregister(Object object) {
        eventBus.unregister(object);
    }

    @Override
    public void post(Object event) {
        eventBus.post(event);
    }

}
