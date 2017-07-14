package com.mercury.platform.shared.hotkey;

import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class HotKeyAdapter extends GlobalKeyAdapter {
    private HotKeyDescriptor currentKey;
    public void keyTyped(HotKeyDescriptor descriptor) {
    }

    @Override
    public void keyPressed(GlobalKeyEvent event) {
        this.currentKey = this.convert(event);
    }

    @Override
    public void keyReleased(GlobalKeyEvent event) {
        if(this.currentKey != null && this.currentKey.equals(this.convert(event))) {
            this.keyTyped(this.currentKey);
        }
    }

    private HotKeyDescriptor convert(GlobalKeyEvent event) {
        HotKeyDescriptor descriptor = new HotKeyDescriptor();
        descriptor.setKeyChar(event.getKeyChar());
        descriptor.setVirtualKeyCode(event.getVirtualKeyCode());
        descriptor.setControlPressed(event.isControlPressed());
        descriptor.setExtendedKey(event.isExtendedKey());
        descriptor.setMenuPressed(event.isMenuPressed());
        descriptor.setShiftPressed(event.isShiftPressed());
        return descriptor;
    }
}
