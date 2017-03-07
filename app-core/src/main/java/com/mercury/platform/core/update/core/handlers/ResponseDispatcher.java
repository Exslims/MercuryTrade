package com.mercury.platform.core.update.core.handlers;

import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.AlertEvent;
import com.mercury.platform.shared.events.custom.ShowPatchNotesEvent;
import com.mercury.platform.shared.events.custom.UpdateInfoEvent;
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
            EventRouter.INSTANCE.fireEvent(new ShowPatchNotesEvent(notes));
        }
        if(object instanceof UpdateDescriptor){
            int nextVersion = ((UpdateDescriptor) object).getVersion();
            EventRouter.INSTANCE.fireEvent(new UpdateInfoEvent(nextVersion));
        }
        if(object instanceof AlreadyLatestUpdateMessage){
            String message = ((AlreadyLatestUpdateMessage) object).getMessage();
            if(ApplicationHolder.getInstance().isManualRequest()) {
                EventRouter.INSTANCE.fireEvent(new AlertEvent(message));
            }
        }
    }
}
