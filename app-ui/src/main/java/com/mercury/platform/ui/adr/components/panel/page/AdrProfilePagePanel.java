package com.mercury.platform.ui.adr.components.panel.page;

import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import com.mercury.platform.ui.adr.validator.FieldValidator;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.dialog.AlertDialog;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;


public class AdrProfilePagePanel extends AdrPagePanel<List<AdrProfileDescriptor>> {
    @Override
    protected void init() {
        JPanel container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.FRAME);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);

        JPanel generalPanel = this.componentsFactory.getJPanel(new GridLayout(0, 1, 0, 6));
        generalPanel.setBackground(AppThemeColor.ADR_BG);
        generalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK),
                BorderFactory.createEmptyBorder(4, 2, 4, 2)));
        this.payload.forEach(it -> {
            generalPanel.add(this.componentsFactory.wrapToSlide(this.getProfilePanel(it), AppThemeColor.ADR_BG, 2, 2, 2, 4));
        });
        container.add(this.componentsFactory.wrapToSlide(generalPanel));
        container.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocus();
            }
        });
        JLabel header = this.componentsFactory.getTextLabel("Profiles:", FontStyle.REGULAR, 20);
        header.setBorder(BorderFactory.createEmptyBorder(4, 6, 0, 4));
        this.add(header, BorderLayout.PAGE_START);
        this.add(verticalContainer, BorderLayout.CENTER);
    }

    private JPanel getProfilePanel(AdrProfileDescriptor descriptor) {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(6, 0));
        root.setBackground(AppThemeColor.ADR_BG);
        JTextField profileNameField = this.adrComponentsFactory.getSmartField(descriptor.getProfileName(), new FieldValidator<String, String>() {
            @Override
            public boolean validate(String value) {
                List<String> existingNames = payload.stream()
                        .map(AdrProfileDescriptor::getProfileName)
                        .collect(Collectors.toList());
                if (!existingNames.contains(value)) {
                    this.value = value;
                }
                return !existingNames.contains(value);
            }
        }, value -> {
            descriptor.setProfileName(value);
            MercuryStoreUI.adrRenameProfileSubject.onNext(true);
        });
        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 16, AppThemeColor.FRAME, TooltipConstants.ADR_EXPORT_BUTTON);
        removeButton.addActionListener(action -> {
            new AlertDialog(success -> {
                if (success) {
                    MercuryStoreUI.adrRemoveProfileSubject.onNext(descriptor);
                    this.removeAll();
                    this.init();
                    MercuryStoreUI.adrManagerRepaint.onNext(true);
                }
            }, "Do you want to delete this profile?", this).setVisible(true);
        });
        if (descriptor.isSelected()) {
            removeButton.setEnabled(false);
        }
        removeButton.setBackground(AppThemeColor.ADR_BG);
        root.add(profileNameField, BorderLayout.CENTER);
        root.add(removeButton, BorderLayout.LINE_END);
        return root;
    }
}
