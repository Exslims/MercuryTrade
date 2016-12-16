package com.home.clicker.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exslims
 * 08.12.2016
 */
public class EventRouter {
    private static final Map<Class, List<SCEventHandler>> eventHandlerMap = new HashMap<>();
    public static void fireEvent(SCEvent event){
        List<SCEventHandler> handlers = eventHandlerMap.get(event.getClass());
        if(handlers != null) {
            handlers.forEach(handler -> handler.handle(event));
        }
    }
    public static void registerHandler(Class eventClass, SCEventHandler handler){
        List<SCEventHandler> scEventHandlers = eventHandlerMap.get(eventClass);
        if(scEventHandlers == null){
            scEventHandlers = new ArrayList<>();
            scEventHandlers.add(handler);
        }
        eventHandlerMap.put(eventClass,scEventHandlers);
    }
    public static void clear(Class eventClass){
        eventHandlerMap.remove(eventClass);
    }
}
