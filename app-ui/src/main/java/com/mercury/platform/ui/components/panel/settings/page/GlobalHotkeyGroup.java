package com.mercury.platform.ui.components.panel.settings.page;


import java.util.ArrayList;
import java.util.List;

public class GlobalHotkeyGroup {
    public static GlobalHotkeyGroup INSTANCE = GlobalHotkeyGroupHolder.HOLDER_INSTANCE;
    private List<HotKeyGroup> groups = new ArrayList<>();

    public void registerGroup(HotKeyGroup group) {
        this.groups.add(group);
    }

    public void onHotKeyChange(HotKeyGroup group, HotKeyPanel panel) {
        this.groups.forEach(it -> {
            if (!it.equals(group)) {
                if (it.isGlobal()) {
                    it.onHotKeyCheck(panel);
                }
            }
        });
    }

    public void onGlobalHotKeyChange(HotKeyGroup group, HotKeyPanel panel) {
        this.groups.forEach(it -> {
            if (!it.equals(group)) {
                it.onHotKeyCheck(panel);
            }
        });
    }

    public void clear() {
        this.groups.clear();
    }

    private static class GlobalHotkeyGroupHolder {
        static final GlobalHotkeyGroup HOLDER_INSTANCE = new GlobalHotkeyGroup();
    }
}
