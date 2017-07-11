package com.mercury.platform.ui.dialog;


import com.mercury.platform.ui.components.ComponentsFactory;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDialog<T,P> extends JDialog {
    protected DialogCallback<T> callback;
    protected P payload;
    protected ComponentsFactory componentsFactory = new ComponentsFactory();
    public BaseDialog(DialogCallback<T> callback, Component relative, P payload) {
        this.payload = payload;
        this.callback = callback;
        this.setModal(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        this.createView();
        this.pack();
        this.setLocationRelativeTo(relative);
        this.setVisible(true);
    }
    protected abstract void createView();
}
