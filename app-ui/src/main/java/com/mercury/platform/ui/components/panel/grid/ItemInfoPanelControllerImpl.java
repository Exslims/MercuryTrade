package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.entity.ItemMessage;
import com.mercury.platform.ui.misc.event.CloseGridItemEvent;
import com.mercury.platform.ui.misc.event.ItemCellStateChangedEvent;
import lombok.NonNull;


public class ItemInfoPanelControllerImpl implements ItemInfoPanelController {
    private ItemMessage message;
    public ItemInfoPanelControllerImpl(@NonNull ItemMessage message) {
        this.message = message;
    }
    @Override
    public void hidePanel() {
        EventRouter.UI.fireEvent(new CloseGridItemEvent(message));
    }

    @Override
    public void changeTabType(@NonNull ItemInfoPanel panel) {
        EventRouter.UI.fireEvent(new ItemCellStateChangedEvent(panel));
    }
}
