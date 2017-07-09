package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.shared.entity.message.ItemMessage;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.NonNull;


public class ItemInfoPanelControllerImpl implements ItemInfoPanelController {
    private ItemMessage message;
    public ItemInfoPanelControllerImpl(@NonNull ItemMessage message) {
        this.message = message;
    }
    @Override
    public void hidePanel() {
        MercuryStoreUI.closeGridItemSubject.onNext(message);
    }

    @Override
    public void changeTabType(@NonNull ItemInfoPanel panel) {
        MercuryStoreUI.itemCellStateSubject.onNext(panel);
    }
}
