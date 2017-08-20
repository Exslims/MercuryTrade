package com.mercury.platform.ui.components.panel.settings.page;

import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class HotKeyPanel extends JPanel {
    private HotKeyDescriptor descriptor;
    private boolean hotKeyAllowed;
    private ComponentsFactory componentsFactory = new ComponentsFactory();
    public HotKeyPanel(HotKeyDescriptor descriptor) {
        super(new BorderLayout());
        this.descriptor = descriptor;
        this.setPreferredSize(new Dimension(100,26));

        JButton button = this.componentsFactory.getBorderedButton(this.descriptor.getTitle());
        button.setFont(this.componentsFactory.getFont(FontStyle.BOLD, 17f));
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    button.setBackground(AppThemeColor.ADR_BG);
                    button.setText("Press any key");
                    hotKeyAllowed = true;
                    button.removeMouseListener(this);
                }
            }
        };
        button.addMouseListener(mouseAdapter);
        MercuryStoreCore.hotKeySubject.subscribe(hotKey -> {
            if (hotKeyAllowed) {
                button.setBackground(AppThemeColor.BUTTON);
                if (hotKey.getVirtualKeyCode() == 27) {
                    this.descriptor.setTitle("...");
                    this.descriptor.setVirtualKeyCode(0);
                    this.descriptor.setMenuPressed(false);
                    this.descriptor.setShiftPressed(false);
                    this.descriptor.setControlPressed(false);
                } else {
                    this.descriptor.setTitle(hotKey.getTitle());
                    this.descriptor.setVirtualKeyCode(hotKey.getVirtualKeyCode());
                    this.descriptor.setMenuPressed(hotKey.isMenuPressed());
                    this.descriptor.setShiftPressed(hotKey.isShiftPressed());
                    this.descriptor.setControlPressed(hotKey.isControlPressed());
                }
                button.setText(hotKey.getTitle());
                hotKeyAllowed = false;
                button.addMouseListener(mouseAdapter);
            }
        });
        this.add(button,BorderLayout.CENTER);
    }
}
