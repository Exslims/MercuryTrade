package com.mercury.platform.ui.components.fields.style;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;


public class MercuryComboBoxUI extends BasicComboBoxUI {
    public static MercuryComboBoxUI createUI(JComponent c) {
        return new MercuryComboBoxUI();
    }

    @Override
    protected JButton createArrowButton() {
        return new ComponentsFactory().getIconButton("app/expand-combobox.png", 16, AppThemeColor.HEADER, "Expand.");
    }

}
