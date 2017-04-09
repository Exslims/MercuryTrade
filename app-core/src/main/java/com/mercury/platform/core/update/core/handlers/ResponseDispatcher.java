package com.mercury.platform.core.update.core.handlers;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.AlertEvent;
import com.mercury.platform.shared.events.custom.ShowPatchNotesEvent;
import com.mercury.platform.shared.events.custom.SoundNotificationEvent;
import com.mercury.platform.shared.events.custom.UpdateInfoEvent;
import com.mercury.platform.shared.store.MercuryStore;
import com.mercury.platform.update.AlreadyLatestUpdateMessage;
import com.mercury.platform.update.PatchNotesDescriptor;
import com.mercury.platform.update.UpdateDescriptor;

/**
 * Need refactoring
 */
public class ResponseDispatcher {
    public void process(Object object){
        if (object instanceof PatchNotesDescriptor) {
            String notes = ((PatchNotesDescriptor) object).getJson();
            EventRouter.CORE.fireEvent(new ShowPatchNotesEvent(notes));
        }
        if(object instanceof UpdateDescriptor){
            MercuryStore.INSTANCE.soundSubject.onNext(SoundType.UPDATE);
            EventRouter.CORE.fireEvent(new UpdateInfoEvent(((UpdateDescriptor) object).getVersion()));
        }
        if(object instanceof AlreadyLatestUpdateMessage){
            if(ApplicationHolder.getInstance().isManualRequest()) {
                ApplicationHolder.getInstance().setManualRequest(false);
                String message = ((AlreadyLatestUpdateMessage) object).getMessage();
                EventRouter.CORE.fireEvent(new AlertEvent(message));
            }
        }
    }
}
