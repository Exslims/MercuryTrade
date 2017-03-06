package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 06.03.2017.
 */
public class ShowPatchNotesEvent implements MercuryEvent {
    private String patchNotes;

    public ShowPatchNotesEvent(String patchNotes) {
        this.patchNotes = patchNotes;
    }

    public String getPatchNotes() {
        return patchNotes;
    }
}
