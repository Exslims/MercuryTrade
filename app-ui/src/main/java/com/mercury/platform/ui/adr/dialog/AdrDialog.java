package com.mercury.platform.ui.adr.dialog;


import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public abstract class AdrDialog<T> extends JDialog {
    protected ComponentsFactory componentsFactory = new ComponentsFactory();
    @Getter
    protected T payload;

    public AdrDialog(Component relative, T payload) {
        this.payload = payload;
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

    private void createView() {
        this.setPreferredSize(new Dimension(600, 500));
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.add(this.getDataPanel(), BorderLayout.CENTER);
        root.add(this.getViewPanel(), BorderLayout.LINE_END);
        this.add(this.componentsFactory.wrapToSlide(root), BorderLayout.CENTER);
    }

    protected abstract JPanel getDataPanel();

    protected abstract JPanel getViewPanel();
}
