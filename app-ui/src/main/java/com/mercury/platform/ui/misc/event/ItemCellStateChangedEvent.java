package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.ui.components.panel.grid.ItemInfoPanel;
import com.mercury.platform.ui.components.panel.grid.TabInfoPanel;

public class ItemCellStateChangedEvent implements MercuryEvent {
    private ItemInfoPanel itemInfoPanel;

    public ItemCellStateChangedEvent(ItemInfoPanel itemInfoPanel) {
        this.itemInfoPanel = itemInfoPanel;
    }

    public ItemInfoPanel getItemInfoPanel() {
        return itemInfoPanel;
    }
}
