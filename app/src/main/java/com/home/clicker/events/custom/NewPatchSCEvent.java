package com.home.clicker.events.custom;

import com.home.clicker.events.SCEvent;

/**
 * Created by Константин on 09.12.2016.
 */
public class NewPatchSCEvent implements SCEvent {
    private String patchTitle;

    public NewPatchSCEvent(String patchTitle) {
        this.patchTitle = patchTitle;
    }

    public String getPatchTitle() {
        return patchTitle;
    }

    public void setPatchTitle(String patchTitle) {
        this.patchTitle = patchTitle;
    }
}
