package com.mercury.platform.ui.components.datatable.renderer;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlainTextRenderer implements MCellRenderer<String> {
    private ComponentsFactory componentsFactory = new ComponentsFactory();
    @Override
    public JComponent getComponent(String data) {
        JTextArea simpleTextArea = this.componentsFactory.getSimpleTextArea(data, FontStyle.REGULAR, 15f);
        simpleTextArea.setBackground(AppThemeColor.TABLE_CELL_BG);
        simpleTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                simpleTextArea.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                simpleTextArea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        return this.componentsFactory.wrapToSlide(simpleTextArea, AppThemeColor.TABLE_CELL_BG, 4, 4, 4, 4);
    }
}
