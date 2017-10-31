package com.mercury.platform.ui.components.datatable.renderer;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlainIconRenderer implements MCellRenderer<String> {
    private ComponentsFactory componentsFactory = new ComponentsFactory();

    @Override
    public JComponent getComponent(String data) {
        String[] split = data.split(":");
        if (split.length == 2) {
            JPanel root = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER, 0, 0), AppThemeColor.TABLE_CELL_BG);
            root.setAlignmentY(JComponent.CENTER_ALIGNMENT);
            String curCountStr = " ";
            if (Double.valueOf(split[0]) > 0) {
                curCountStr = Double.valueOf(split[0]) % 1 == 0 ?
                        String.valueOf(Double.valueOf(split[0]).intValue()) :
                        String.valueOf(Double.valueOf(split[0]));
            }
            root.add(this.componentsFactory.getTextLabel(curCountStr, FontStyle.REGULAR));
            JLabel iconLabel = componentsFactory.getIconLabel("currency/" + split[1] + ".png", 26);
            root.add(iconLabel);
            return root;
        } else {
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
}
