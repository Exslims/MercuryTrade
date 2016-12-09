package com.home.clicker.events.custom;

import com.home.clicker.events.SCEvent;

import java.util.List;

/**
 * Exslims
 * 08.12.2016
 */
public class ActualWritersChangeEvent implements SCEvent {
    private List<String> writers;

    public ActualWritersChangeEvent(List<String> writers) {
        this.writers = writers;
    }

    public List<String> getWriters() {
        return writers;
    }

    public void setWriters(List<String> writers) {
        this.writers = writers;
    }
}
