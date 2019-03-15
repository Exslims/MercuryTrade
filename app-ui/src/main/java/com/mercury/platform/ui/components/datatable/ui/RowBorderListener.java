package com.mercury.platform.ui.components.datatable.ui;

import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RowBorderListener extends MouseAdapter {
    private JComponent component;

    public RowBorderListener(JComponent component) {
        this.component = component;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        component.setBorder(BorderFactory.createLineBorder(AppThemeColor.TABLE_MOUSEOVER_BORDER));
        component.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        component.setBorder(BorderFactory.createLineBorder(AppThemeColor.TABLE_BORDER));
        component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}
