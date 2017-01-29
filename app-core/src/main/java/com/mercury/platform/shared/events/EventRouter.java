package com.mercury.platform.shared.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Exslims
 * 08.12.2016
 */
public class EventRouter {
    private static class EventRouterHolder {
        static final EventRouter HOLDER_INSTANCE = new EventRouter();
    }
    public static EventRouter INSTANCE = EventRouterHolder.HOLDER_INSTANCE;

    private Map<Class, List<MercuryEventHandler>> eventHandlerMap = new ConcurrentHashMap<>();
    public void fireEvent(MercuryEvent event){
        List<MercuryEventHandler> handlers = eventHandlerMap.get(event.getClass());
        if(handlers != null) {
            handlers.forEach(handler -> handler.handle(event));
        }
    }
    public void registerHandler(Class eventClass, MercuryEventHandler handler){
        List<MercuryEventHandler> mercuryEventHandlers = eventHandlerMap.get(eventClass);
        if(mercuryEventHandlers == null){
            mercuryEventHandlers = new ArrayList<>();
        }
        mercuryEventHandlers.add(handler);
        eventHandlerMap.put(eventClass, mercuryEventHandlers);
    }
}
