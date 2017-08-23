package com.mercury.platform.shared.hotkey;

import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

public class MercuryNativeMouseListener implements NativeMouseListener {
    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
        HotKeyDescriptor hotKeyDescriptor = new HotKeyDescriptor();
        hotKeyDescriptor.setVirtualKeyCode(nativeMouseEvent.getButton() + 10000);
        hotKeyDescriptor.setTitle(this.getModifiersText(nativeMouseEvent.getButton()));
        if (!hotKeyDescriptor.getTitle().equals("Mouse left")) {
            MercuryStoreCore.hotKeySubject.onNext(hotKeyDescriptor);
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
    }

    private String getModifiersText(int code) {
        switch (code) {
            case 1: {
                return "Mouse left";
            }
            case 2: {
                return "Mouse right";
            }
            case 3: {
                return "Mouse middle";
            }
            case 4: {
                return "Button 4";
            }
            case 5: {
                return "Button 5";
            }
            case 6: {
                return "Button 6";
            }
            case 7: {
                return "Button 7";
            }
            default: {
                return "Undefined";
            }
        }
    }
}
