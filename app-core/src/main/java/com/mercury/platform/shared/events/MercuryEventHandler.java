package com.mercury.platform.shared.events;


/**
 * Exslims
 * 08.12.2016
 */
public interface MercuryEventHandler<T extends MercuryEvent> {
    void handle(T event);
}
