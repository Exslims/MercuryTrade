package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 09.12.2016.
 */
public class NewPatchMercuryEvent implements MercuryEvent {
    private String patchTitle;

    public NewPatchMercuryEvent(String patchTitle) {
        this.patchTitle = patchTitle;
    }

    public String getPatchTitle() {
        return patchTitle;
    }

    public void setPatchTitle(String patchTitle) {
        this.patchTitle = patchTitle;
    }
}
