package com.mercury.platform.shared.hotkey;

import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class MercuryNativeKeyListener implements NativeKeyListener {
    private boolean menuPressed;
    private boolean shiftPressed;
    private boolean ctrlpressed;

    private boolean block;

    public MercuryNativeKeyListener() {
        MercuryStoreCore.blockHotkeySubject.subscribe(state -> this.block = state);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        switch (nativeKeyEvent.getKeyCode()) {
            case 42: {
                shiftPressed = true;
                break;
            }
            case 29: {
                ctrlpressed = true;
                break;
            }
            case 56: {
                menuPressed = true;
                break;
            }
            default: {
                if (!this.block) {
                    MercuryStoreCore.hotKeySubject.onNext(this.getDescriptor(nativeKeyEvent));
                }
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        if (!this.block) {
            MercuryStoreCore.hotKeyReleaseSubject.onNext(this.getDescriptor(nativeKeyEvent));
        }
        switch (nativeKeyEvent.getKeyCode()) {
            case 42: {
                shiftPressed = false;
                break;
            }
            case 29: {
                ctrlpressed = false;
                break;
            }
            case 56: {
                menuPressed = false;
                break;
            }
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    }

    private String getButtonText(HotKeyDescriptor descriptor) {
        String text = descriptor.getTitle();
        if (descriptor.isShiftPressed())
            text = "Shift + " + text;
        if (descriptor.isMenuPressed())
            text = "Alt + " + text;
        if (descriptor.isControlPressed())
            text = "Ctrl + " + text;
        return text;
    }

    private HotKeyDescriptor getDescriptor(NativeKeyEvent nativeKeyEvent) {
        HotKeyDescriptor hotKeyDescriptor = new HotKeyDescriptor();
        hotKeyDescriptor.setTitle(NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));
        hotKeyDescriptor.setVirtualKeyCode(nativeKeyEvent.getKeyCode());
        hotKeyDescriptor.setControlPressed(ctrlpressed);
        hotKeyDescriptor.setShiftPressed(shiftPressed);
        hotKeyDescriptor.setMenuPressed(menuPressed);

        hotKeyDescriptor.setTitle(this.getButtonText(hotKeyDescriptor));
        return hotKeyDescriptor;
    }
}
