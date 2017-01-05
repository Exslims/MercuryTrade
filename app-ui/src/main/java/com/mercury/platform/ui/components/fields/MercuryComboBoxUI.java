package com.mercury.platform.ui.components.fields;

import com.mercury.platform.ui.components.ComponentsFactory;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * Created by Константин on 04.01.2017.
 */
public class MercuryComboBoxUI extends BasicComboBoxUI {
    public static MercuryComboBoxUI createUI(JComponent c) {
        return new MercuryComboBoxUI();
    }

    @Override protected JButton createArrowButton() {
        return ComponentsFactory.INSTANCE.getIconButton("app/expand-combobox.png",16);
    }
}
