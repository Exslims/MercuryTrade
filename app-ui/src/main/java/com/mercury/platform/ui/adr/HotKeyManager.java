package com.mercury.platform.ui.adr;


import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HotKeyManager {
    private boolean allowed;
    private ComponentsFactory factory = new ComponentsFactory();
    public JButton getHotKeyButton(HotKeyDescriptor descriptor){
        JButton button = this.factory.getBorderedButton(this.getButtonText(descriptor));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                button.setBackground(AppThemeColor.SLIDE_BG);
                allowed = true;
            }
        });
        MercuryStoreCore.hotKeySubject.subscribe(hotKey -> {
            if(allowed) {
                button.setBackground(AppThemeColor.BUTTON);

                descriptor.setVirtualKeyCode(hotKey.getVirtualKeyCode());
                descriptor.setKeyChar(hotKey.getKeyChar());
                descriptor.setShiftPressed(hotKey.isShiftPressed());
                descriptor.setMenuPressed(hotKey.isMenuPressed());
                descriptor.setExtendedKey(hotKey.isExtendedKey());
                descriptor.setControlPressed(hotKey.isControlPressed());

                button.setText(getButtonText(hotKey));
                allowed = false;
            }
        });
        return button;
    }
    private String getButtonText(HotKeyDescriptor descriptor){
        String text = String.valueOf(descriptor.getKeyChar());
        if(descriptor.isShiftPressed())
            text = "Shift + " + text;
        if(descriptor.isMenuPressed())
            text = "Alt + " + text;
        if(descriptor.isControlPressed())
            text = "Ctrl + " + text;
        return text;
    }
}
