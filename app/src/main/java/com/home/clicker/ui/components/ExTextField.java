package com.home.clicker.ui.components;

import com.home.clicker.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 16.12.2016.
 */
public class ExTextField extends JTextField {
    public ExTextField(String text) {
        super();
        this.setText(text);
        init();
    }

    private void init() {
        this.setFont(new Font("Tahoma", Font.BOLD, 14));
        this.setForeground(AppThemeColor.TEXT_DEFAULT);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TEXT_DEFAULT,1),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,3)
        ));
    }
}
