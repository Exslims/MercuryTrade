package com.home.clicker.events;


/**
 * Exslims
 * 08.12.2016
 */
public interface SCEventHandler<T extends SCEvent> {
    void handle(T event);
}
