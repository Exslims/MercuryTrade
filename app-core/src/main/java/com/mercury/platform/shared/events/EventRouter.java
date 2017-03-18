package com.mercury.platform.shared.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Exslims
 * 08.12.2016
 */
public class EventRouter {
    private static final Logger LOGGER = LogManager.getLogger(EventRouter.class.getSimpleName());
    private static class EventRouterHolder {
        static final EventRouter CORE_INSTANCE = new EventRouter();
        static final EventRouter UI_INSTANCE = new EventRouter();
    }
    public static EventRouter CORE = EventRouterHolder.CORE_INSTANCE;
    public static EventRouter UI = EventRouterHolder.UI_INSTANCE;

    private ReentrantLock lock = new ReentrantLock();

    private Map<Class, List<MercuryEventHandler>> eventHandlerMap;
    private EventRouter() {
        eventHandlerMap = new ConcurrentHashMap<>();
    }
    public void fireEvent(MercuryEvent event){
        lock.lock();
        List<MercuryEventHandler> handlers = eventHandlerMap.get(event.getClass());
        if(handlers != null) {
            try {
                handlers.forEach(handler -> {
                    handler.handle(event);
                });
            }catch (ConcurrentModificationException e){
                LOGGER.error("Event: " + event.getClass().getSimpleName(),e);
            }
        }
        lock.unlock();
    }
    public void registerHandler(Class eventClass, MercuryEventHandler handler){
        lock.lock();
        List<MercuryEventHandler> mercuryEventHandlers = eventHandlerMap.get(eventClass);
        if(mercuryEventHandlers == null){
            mercuryEventHandlers = new ArrayList<>();
        }
        mercuryEventHandlers.add(handler);
        eventHandlerMap.put(eventClass, mercuryEventHandlers);
        lock.unlock();
    }
    public void unregisterHandler(Class eventClass, MercuryEventHandler handler){
        lock.lock();
        List<MercuryEventHandler> mercuryEventHandlers = eventHandlerMap.get(eventClass);
        mercuryEventHandlers.remove(handler);
        eventHandlerMap.put(eventClass, mercuryEventHandlers);
        lock.unlock();
    }
}
