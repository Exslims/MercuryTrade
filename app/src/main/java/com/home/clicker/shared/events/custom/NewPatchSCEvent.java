package com.home.clicker.shared.events.custom;

import com.home.clicker.shared.events.SCEvent;

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
