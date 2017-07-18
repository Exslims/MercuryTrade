package com.mercury.platform.shared.hotkey;


import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import lc.kra.system.keyboard.GlobalKeyboardHook;

public class HotKeysInterceptor {
    public HotKeysInterceptor() {
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(false);
        keyboardHook.addKeyListener(new HotKeyAdapter() {
            @Override
            public void keyTyped(HotKeyDescriptor descriptor) {
                MercuryStoreCore.hotKeySubject.onNext(descriptor);
            }
        });
    }
}
