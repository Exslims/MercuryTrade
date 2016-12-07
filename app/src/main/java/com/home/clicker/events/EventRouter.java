package com.home.clicker.events;

import java.util.HashMap;
import java.util.Map;

/**
 * Exslims
 * 08.12.2016
 */
public class EventRouter {
    private static final Map<Class, EventHandler> eventHandlerMap = new HashMap<Class, EventHandler>();
    public static void fireEvent(Event event){
        eventHandlerMap.get(event.getClass()).handle(event);
    }
    public static void registerHandler(Class eventClass, EventHandler handler){
        eventHandlerMap.put(eventClass,handler);
    }
}
