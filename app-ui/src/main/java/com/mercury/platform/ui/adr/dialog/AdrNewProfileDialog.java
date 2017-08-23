package com.mercury.platform.ui.adr.dialog;

import com.mercury.platform.ui.adr.components.AdrComponentsFactory;
import com.mercury.platform.ui.adr.validator.ProfileNameValidator;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.dialog.BaseDialog;
import com.mercury.platform.ui.dialog.DialogCallback;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class AdrNewProfileDialog extends BaseDialog<String, List<String>> {
    private AdrComponentsFactory adrComponentsFactory;

    public AdrNewProfileDialog(DialogCallback<String> callback, Component relative, List<String> payload) {
        super(callback, relative, payload);
        this.setTitle("Create new profile");
        this.setResizable(false);
    }

    @Override
    protected void createView() {
        this.adrComponentsFactory = new AdrComponentsFactory(this.componentsFactory);
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(AppThemeColor.ADR_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));

        ProfileNameValidator profileNameValidator = new ProfileNameValidator(this.payload);
        JTextField nameField = this.componentsFactory.getTextField("", FontStyle.REGULAR, 16f);
        nameField.setPreferredSize(new Dimension(300, 20));
        JPanel fieldPanel = this.componentsFactory.getJPanel(new BorderLayout());
        fieldPanel.add(this.componentsFactory.getTextLabel("Profile name: "), BorderLayout.LINE_START);
        fieldPanel.add(nameField, BorderLayout.CENTER);
        fieldPanel.setBackground(AppThemeColor.ADR_BG);

        JPanel buttonsPanel = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setBackground(AppThemeColor.ADR_BG);
        JButton createButton = this.componentsFactory.getBorderedButton("Create");
        createButton.setPreferredSize(new Dimension(100, 26));
        createButton.setFont(this.componentsFactory.getFont(FontStyle.BOLD, 16f));
        createButton.addActionListener(action -> {
            if (!nameField.getText().isEmpty() && profileNameValidator.validate(nameField.getText())) {
                this.callback.onAction(nameField.getText());
                this.setVisible(false);
                this.dispose();
            }
        });
        JButton cancelButton = componentsFactory.getButton(
                FontStyle.BOLD,
                AppThemeColor.FRAME,
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                "Cancel",
                16f);
        cancelButton.setPreferredSize(new Dimension(100, 26));
        cancelButton.addActionListener(action -> {
            this.setVisible(false);
        });
        buttonsPanel.add(createButton);
        buttonsPanel.add(cancelButton);

        root.add(this.componentsFactory.wrapToSlide(fieldPanel, AppThemeColor.ADR_BG), BorderLayout.CENTER);
        root.add(this.componentsFactory.wrapToSlide(buttonsPanel, AppThemeColor.ADR_BG), BorderLayout.PAGE_END);
        this.add(root, BorderLayout.CENTER);
    }
}
