package com.mercury.platform.shared.hotkey;


import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

public class HotKeysInterceptor {
    public HotKeysInterceptor() {
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(false);
        keyboardHook.addKeyListener(new HotKeyAdapter());

        GlobalMouseHook mouseHook = new GlobalMouseHook(false);
        mouseHook.addMouseListener(new GlobalMouseAdapter() {
            @Override
            public void mousePressed(GlobalMouseEvent globalMouseEvent) {
                MercuryStoreCore.hotKeySubject.onNext(this.convert(globalMouseEvent));
            }
            private HotKeyDescriptor convert(GlobalMouseEvent event) {
                HotKeyDescriptor descriptor = new HotKeyDescriptor();
                switch (event.getButton()){
                    case 1: {
                        descriptor.setVirtualKeyCode(1000);
                        descriptor.setTitle("Mouse left");
                        break;
                    }
                    case 2: {
                        descriptor.setVirtualKeyCode(1002);
                        descriptor.setTitle("Mouse right");
                        break;
                    }
                    case 16: {
                        descriptor.setVirtualKeyCode(1016);
                        descriptor.setTitle("Mouse middle");
                        break;
                    }
                }
                return descriptor;
            }
        });
    }

    public static void main(String[] args) {
        new HotKeysInterceptor();
    }
}
