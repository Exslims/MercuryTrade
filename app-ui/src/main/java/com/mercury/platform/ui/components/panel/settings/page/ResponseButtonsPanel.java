package com.mercury.platform.ui.components.panel.settings.page;

import com.mercury.platform.shared.config.descriptor.ResponseButtonDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;


public class ResponseButtonsPanel extends JPanel implements ViewInit {
    private List<ResponseButtonDescriptor> buttons;
    private ComponentsFactory componentsFactory = new ComponentsFactory();
    private HotKeyGroup hotKeyGroup;

    public ResponseButtonsPanel(List<ResponseButtonDescriptor> buttons, HotKeyGroup hotKeyGroup) {
        super(new BorderLayout(4, 4));
        this.hotKeyGroup = hotKeyGroup;
        this.buttons = buttons;
    }

    @Override
    public void onViewInit() {
        this.setBackground(AppThemeColor.SETTINGS_BG);
        JPanel buttonsTable = this.componentsFactory.getJPanel(new GridLayout(0, 1, 4, 4), AppThemeColor.SETTINGS_BG);
        buttonsTable.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));

        JPanel headerPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.SETTINGS_BG);

        JLabel titleLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_DEFAULT, null, 15f, "Label");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setPreferredSize(new Dimension(120, 26));
        JLabel valueLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_DEFAULT, null, 15f, "Response text");
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel hotKeyLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_DEFAULT, null, 15f, "");
        hotKeyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hotKeyLabel.setPreferredSize(new Dimension(130, 20));

        JLabel closeLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_DEFAULT, null, 15f, "");
        closeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(titleLabel, BorderLayout.LINE_START);
        headerPanel.add(valueLabel, BorderLayout.CENTER);

        JPanel miscPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.SETTINGS_BG);
        miscPanel.add(hotKeyLabel, BorderLayout.CENTER);
        miscPanel.add(closeLabel, BorderLayout.LINE_END);
        headerPanel.add(miscPanel, BorderLayout.LINE_END);

        buttonsTable.add(headerPanel);

        buttons.forEach(it -> {
            buttonsTable.add(this.getResponseRow(it));
        });
        this.add(buttonsTable, BorderLayout.CENTER);
        JButton addButton = this.componentsFactory.getIconButton("app/add_button.png", 22, AppThemeColor.HEADER, "Add button");
        addButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        addButton.addActionListener(action -> {
            ResponseButtonDescriptor descriptor = new ResponseButtonDescriptor();
            int size = buttons.size();
            descriptor.setId(++size);
            buttons.add(descriptor);
            buttonsTable.add(this.getResponseRow(descriptor));
            MercuryStoreUI.settingsRepaintSubject.onNext(true);
            MercuryStoreUI.settingsPackSubject.onNext(true);
        });
        this.add(buttonsTable, BorderLayout.CENTER);
        this.add(addButton, BorderLayout.PAGE_END);
    }

    private JPanel getResponseRow(ResponseButtonDescriptor descriptor) {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(4, 4), AppThemeColor.SETTINGS_BG);
        JTextField titleField = this.componentsFactory.getTextField(descriptor.getTitle(), FontStyle.REGULAR, 15f);
        titleField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                descriptor.setTitle(titleField.getText());
            }
        });
        titleField.setPreferredSize(new Dimension(120, 26));
        JTextField responseField = this.componentsFactory.getTextField(descriptor.getResponseText(), FontStyle.REGULAR, 15f);
        responseField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                descriptor.setResponseText(responseField.getText());
            }
        });
        root.add(this.componentsFactory.wrapToSlide(titleField, AppThemeColor.SETTINGS_BG, 0, 2, 2, 0), BorderLayout.LINE_START);
        root.add(this.componentsFactory.wrapToSlide(responseField, AppThemeColor.SETTINGS_BG, 0, 0, 2, 0), BorderLayout.CENTER);

        JPanel miscPanel = this.componentsFactory.getJPanel(new BorderLayout(4, 4), AppThemeColor.SETTINGS_BG);
        JCheckBox checkBox = this.componentsFactory.getCheckBox(descriptor.isClose(), "Close notification panel after click?");
        checkBox.addActionListener(action -> {
            descriptor.setClose(checkBox.isSelected());
        });
        miscPanel.add(checkBox, BorderLayout.LINE_START);
        HotKeyPanel hotKeyPanel = new HotKeyPanel(descriptor.getHotKeyDescriptor());
        this.hotKeyGroup.registerHotkey(hotKeyPanel);
        miscPanel.add(this.componentsFactory.wrapToSlide(hotKeyPanel, AppThemeColor.SETTINGS_BG, 0, 0, 2, 0), BorderLayout.CENTER);

        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 17, AppThemeColor.SETTINGS_BG, "Remove button");
        removeButton.addActionListener(action -> {
            root.getParent().remove(root);
            this.buttons.remove(descriptor);
            MercuryStoreUI.settingsPackSubject.onNext(true);
            MercuryStoreUI.settingsRepaintSubject.onNext(true);
        });
        miscPanel.add(removeButton, BorderLayout.LINE_END);

        root.add(miscPanel, BorderLayout.LINE_END);
        return root;
    }
}
