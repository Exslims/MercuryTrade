package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

public class ShowPatchNotesEvent implements MercuryEvent {
    private String patchNotes;

    public ShowPatchNotesEvent(String patchNotes) {
        this.patchNotes = patchNotes;
    }

    public String getPatchNotes() {
        return patchNotes;
    }
}
