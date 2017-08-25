package com.mercury.platform.ui.components.panel.settings.page;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class HotKeyGroup {
    private List<HotKeyPanel> hotKeyPanels = new ArrayList<>();
    @Getter
    private boolean global;

    public HotKeyGroup(boolean global) {
        this.global = global;
        GlobalHotkeyGroup.INSTANCE.registerGroup(this);
    }

    public HotKeyGroup() {
        this(false);
    }

    public void registerHotkey(HotKeyPanel panel) {
        panel.setMyGroup(this);
        this.hotKeyPanels.add(panel);
    }

    public void onHotKeyChange(HotKeyPanel panel) {
        if (this.global) {
            GlobalHotkeyGroup.INSTANCE.onGlobalHotKeyChange(this, panel);
        } else {
            GlobalHotkeyGroup.INSTANCE.onHotKeyChange(this, panel);
        }
        this.hotKeyPanels.forEach(it -> {
            if (!it.equals(panel) && it.getDescriptor().equals(panel.getDescriptor()) && it.getDescriptor().getVirtualKeyCode() != -1) {
                it.toDefaultHotkey();
            }
        });
    }

    public void onHotKeyCheck(HotKeyPanel panel) {
        this.hotKeyPanels.forEach(it -> {
            if (!it.equals(panel) && it.getDescriptor().equals(panel.getDescriptor()) && it.getDescriptor().getVirtualKeyCode() != -1) {
                it.toDefaultHotkey();
            }
        });
    }
}
