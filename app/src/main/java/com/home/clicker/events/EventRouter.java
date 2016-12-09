package com.home.clicker.events;

import java.util.HashMap;
import java.util.Map;

/**
 * Exslims
 * 08.12.2016
 */
public class EventRouter {
    private static final Map<Class, SCEventHandler> eventHandlerMap = new HashMap<Class, SCEventHandler>();
    public static void fireEvent(SCEvent SCEvent){
        eventHandlerMap.get(SCEvent.getClass()).handle(SCEvent);
    }
    public static void registerHandler(Class eventClass, SCEventHandler handler){
        eventHandlerMap.put(eventClass,handler);
    }
}
