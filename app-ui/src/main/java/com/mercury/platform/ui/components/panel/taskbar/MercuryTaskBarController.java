package com.mercury.platform.ui.components.panel.taskbar;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChatCommandEvent;
import com.mercury.platform.shared.events.custom.DndModeEvent;
import com.mercury.platform.shared.events.custom.NotificationEvent;
import com.mercury.platform.ui.frame.movable.ItemsGridFrame;
import com.mercury.platform.ui.frame.titled.chat.ChatFilterFrame;
import com.mercury.platform.ui.frame.titled.container.HistoryFrame;
import com.mercury.platform.ui.frame.titled.SettingsFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.event.RepaintEvent;

public class MercuryTaskBarController implements TaskBarController {
    @Override
    public void enableDND() {
        EventRouter.UI.fireEvent(new RepaintEvent.RepaintTaskBar());
        EventRouter.UI.fireEvent(new NotificationEvent("DND on"));
        EventRouter.CORE.fireEvent(new DndModeEvent(true));
    }

    @Override
    public void disableDND() {
        EventRouter.UI.fireEvent(new RepaintEvent.RepaintTaskBar());
        EventRouter.UI.fireEvent(new NotificationEvent("DND off"));
        EventRouter.CORE.fireEvent(new DndModeEvent(false));
    }

    @Override
    public void showITH() {
        FramesManager.INSTANCE.enableOrDisableMovementDirect(ItemsGridFrame.class);
    }

    @Override
    public void performHideout() {
        EventRouter.CORE.fireEvent(new ChatCommandEvent("/hideout"));
    }

    @Override
    public void showChatFiler() {
        FramesManager.INSTANCE.hideOrShowFrame(ChatFilterFrame.class);
    }

    @Override
    public void showHistory() {
        FramesManager.INSTANCE.hideOrShowFrame(HistoryFrame.class);
    }

    @Override
    public void openPINSettings() {
        FramesManager.INSTANCE.enableMovementExclude(ItemsGridFrame.class);
    }

    @Override
    public void openScaleSettings() {
        FramesManager.INSTANCE.enableScale();
    }

    @Override
    public void showSettings() {
        FramesManager.INSTANCE.showFrame(SettingsFrame.class);
    }

    @Override
    public void exit() {
        FramesManager.INSTANCE.exit();
    }
}
