package com.mercury.platform.ui.adr.dialog;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class AdrDialog<T extends AdrComponentDescriptor> extends JDialog{
    protected ComponentsFactory componentsFactory = new ComponentsFactory();
    protected List<T> payload;
    public AdrDialog(Component relative, List<T> payload) {
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
    }
    protected abstract void createView();
}
