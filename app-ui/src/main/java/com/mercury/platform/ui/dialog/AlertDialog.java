package com.mercury.platform.ui.dialog;

import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;


public class AlertDialog extends BaseDialog<Boolean, String> {
    public AlertDialog(DialogCallback<Boolean> callback, String text, Component relative) {
        super(callback, relative, text);
    }

    protected void createView() {
        this.setLayout(new BorderLayout());
        this.getRootPane().setBackground(AppThemeColor.FRAME_RGB);
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(AppThemeColor.SLIDE_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.MSG_HEADER_BORDER));
        JLabel header = this.componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_NICKNAME, TextAlignment.CENTER, 18f, this.payload);
        header.setBackground(AppThemeColor.FRAME_RGB);
        root.add(header, BorderLayout.CENTER);

        JPanel miscPanel = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER));
        miscPanel.setBackground(AppThemeColor.SLIDE_BG);
        miscPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        JButton okButton = this.componentsFactory.getBorderedButton("Yes", 16);
        okButton.setPreferredSize(new Dimension(120, 26));
        okButton.addActionListener(action -> {
            this.callback.onAction(true);
            this.setVisible(false);
            this.dispose();
        });
        JButton cancelButton = this.componentsFactory.getBorderedButton("No", 16);
        cancelButton.addActionListener(action -> {
            this.callback.onAction(false);
            this.setVisible(false);
            this.dispose();
        });
        cancelButton.setPreferredSize(new Dimension(120, 26));

        miscPanel.add(okButton);
        miscPanel.add(cancelButton);
        root.add(miscPanel, BorderLayout.PAGE_END);

        this.setResizable(false);
        this.add(root, BorderLayout.CENTER);
    }
}
