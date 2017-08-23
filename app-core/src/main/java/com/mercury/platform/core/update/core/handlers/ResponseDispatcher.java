package com.mercury.platform.core.update.core.handlers;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.update.AlreadyLatestUpdateMessage;
import com.mercury.platform.update.PatchNotesDescriptor;
import com.mercury.platform.update.UpdateDescriptor;

public class ResponseDispatcher {
    public void process(Object object) {
        if (object instanceof PatchNotesDescriptor) {
            String notes = ((PatchNotesDescriptor) object).getJson();
            MercuryStoreCore.showPatchNotesSubject.onNext(notes);
        }
        if (object instanceof UpdateDescriptor) {
            MercuryStoreCore.soundSubject.onNext(SoundType.UPDATE);
            MercuryStoreCore.updateInfoSubject.onNext(((UpdateDescriptor) object).getVersion());
        }
        if (object instanceof AlreadyLatestUpdateMessage) {
            if (ApplicationHolder.getInstance().isManualRequest()) {
                ApplicationHolder.getInstance().setManualRequest(false);
                String message = ((AlreadyLatestUpdateMessage) object).getMessage();
                MercuryStoreCore.stringAlertSubject.onNext(message);
            }
        }
    }
}
