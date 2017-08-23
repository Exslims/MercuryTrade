package com.mercury.platform.ui.dialog;


import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDialog<T, P> extends JDialog {
    @Setter
    protected DialogCallback<T> callback;
    protected P payload;
    protected ComponentsFactory componentsFactory = new ComponentsFactory();

    public BaseDialog(DialogCallback<T> callback, Component relative, P payload) {
        this.payload = payload;
        this.callback = callback;
        this.setModal(true);
        this.setAlwaysOnTop(true);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBackground(AppThemeColor.FRAME_RGB);
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        this.createView();
        this.pack();
        this.setLocationRelativeTo(relative);
    }

    protected abstract void createView();
}
