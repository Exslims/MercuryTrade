package com.mercury.platform.server.bus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Frost on 25.01.2017.
 */
public class UpdaterServerAsyncEventBus extends EventBus {

    private static final UpdaterServerAsyncEventBus instance = new UpdaterServerAsyncEventBus();
    private static final Logger LOGGER = LogManager.getLogger(UpdaterServerAsyncEventBus.class);

    public static UpdaterServerAsyncEventBus getInstance() {
        return instance;
    }

    private volatile EventBus eventBus;


    private UpdaterServerAsyncEventBus() {
        ExecutorService service = Executors.newCachedThreadPool();
        SubscriberExceptionHandler handler = (throwable, subscriberExceptionContext) -> LOGGER.error(throwable);
        this.eventBus = new AsyncEventBus(service, handler);
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
