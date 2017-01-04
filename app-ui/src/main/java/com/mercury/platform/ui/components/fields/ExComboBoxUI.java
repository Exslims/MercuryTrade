package com.mercury.platform.ui.components.fields;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

/**
 * Created by Константин on 04.01.2017.
 */
public class ExComboBoxUI extends BasicComboBoxUI {
    public static ExComboBoxUI createUI(JComponent c) {
        return new ExComboBoxUI();
    }

    @Override protected JButton createArrowButton() {
        return ComponentsFactory.INSTANCE.getIconButton("app/expand-combobox.png",16);
    }
}
