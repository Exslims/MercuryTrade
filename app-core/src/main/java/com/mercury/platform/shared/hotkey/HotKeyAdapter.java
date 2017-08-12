package com.mercury.platform.shared.hotkey;

import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class HotKeyAdapter extends GlobalKeyAdapter {
    @Override
    public void keyPressed(GlobalKeyEvent event) {
        MercuryStoreCore.hotKeySubject.onNext(this.convert(event));
    }

    private HotKeyDescriptor convert(GlobalKeyEvent event) {
        HotKeyDescriptor descriptor = new HotKeyDescriptor();
        descriptor.setKeyChar(event.getKeyChar());
        descriptor.setVirtualKeyCode(event.getVirtualKeyCode());
        descriptor.setControlPressed(event.isControlPressed());
        descriptor.setMenuPressed(event.isMenuPressed());
        descriptor.setShiftPressed(event.isShiftPressed());
        return descriptor;
    }
}
