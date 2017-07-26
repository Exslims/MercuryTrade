package com.mercury.platform.ui.adr.dialog;


import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public abstract class AdrDialog<T> extends JDialog{
    protected ComponentsFactory componentsFactory = new ComponentsFactory();
    protected T payload;
    public AdrDialog(Component relative, T payload) {
        this.payload = payload;
        this.setModal(true);
        this.setAlwaysOnTop(true);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBackground(AppThemeColor.FRAME_RGB);
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        this.createView();
        this.pack();
        this.setLocationRelativeTo(relative);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                postConstruct();
            }
        });
    }

    protected abstract void createView();
    protected abstract void postConstruct();
}
