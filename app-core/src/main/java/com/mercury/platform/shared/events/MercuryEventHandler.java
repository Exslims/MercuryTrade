package com.mercury.platform.shared.events;


public interface MercuryEventHandler<T extends MercuryEvent> {
    void handle(T event);
}
