package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.ui.components.panel.grid.TabInfoPanel;

public class DismissStashTabInfoEvent implements MercuryEvent {
    private TabInfoPanel tabInfoPanel;

    public DismissStashTabInfoEvent(TabInfoPanel tabInfoPanel) {
        this.tabInfoPanel = tabInfoPanel;
    }

    public TabInfoPanel getTabInfoPanel() {
        return tabInfoPanel;
    }
}
