package com.mercury.platform.ui.components.datatable.renderer;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

public class PlainIconRenderer implements MCellRenderer<String> {
    private ComponentsFactory componentsFactory = new ComponentsFactory();

    @Override
    public JComponent getComponent(String data) {
        String[] split = data.split(":");
        JPanel root = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER, 0, 0), AppThemeColor.TABLE_CELL_BG);
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
    }
}
