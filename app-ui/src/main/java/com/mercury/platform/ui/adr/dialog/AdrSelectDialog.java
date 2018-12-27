package com.mercury.platform.ui.adr.dialog;


import com.mercury.platform.shared.config.configration.IconBundleConfigurationService;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.dialog.BaseDialog;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AdrSelectDialog extends BaseDialog<String, String[]> {
    protected JList iconsList;
    protected IconBundleConfigurationService config;

    public void setSelectedIcon(String iconPath) {
        this.iconsList.setSelectedValue(iconPath, true);
    }

    public AdrSelectDialog() {
        super(null, null, null);
        this.setTitle("Select");
    }

    protected JPanel getFilterPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.add(this.componentsFactory.getTextLabel("Filter:"), BorderLayout.LINE_START);
        JTextField filterField = this.componentsFactory.getTextField("Filter:", FontStyle.REGULAR, 15);
        filterField.addActionListener(action -> {
            List<String> entities = this.config.getDefaultBundle();
            entities.addAll(this.config.getEntities());
            if (filterField.getText().equals("")) {
                this.iconsList.setListData(entities.toArray());
            } else {
                List<String> collect = entities.stream()
                        .filter(name -> StringUtils.containsIgnoreCase(name, filterField.getText()))
                        .collect(Collectors.toList());
                this.iconsList.setListData(collect.toArray());
            }
        });
        root.add(filterField, BorderLayout.CENTER);
        root.setBackground(AppThemeColor.SLIDE_BG);
        root.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK),
                        BorderFactory.createEmptyBorder(4, 0, 4, 4)));
        JButton addIconButton = this.componentsFactory.getBorderedButton("Add icon");
        addIconButton.setPreferredSize(new Dimension(100, 26));
        addIconButton.addActionListener(action -> {
            FileDialog dialog = new FileDialog(this, "Choose icon", FileDialog.LOAD);
            dialog.setFile("*.png");
            dialog.setVisible(true);
            dialog.toFront();
            if (this.isValidIconPath(dialog.getFile())) {
                this.config.addIcon(dialog.getDirectory() + dialog.getFile());
                Object selectedValue = this.iconsList.getSelectedValue();
                List<String> entities = this.config.getDefaultBundle();
                entities.addAll(this.config.getEntities());
                this.iconsList.setListData(entities.toArray());
                this.setSelectedIcon((String) selectedValue);
                MercuryStoreCore.saveConfigSubject.onNext(true);
            }
        });
        root.add(this.componentsFactory.wrapToSlide(addIconButton, AppThemeColor.ADR_BG), BorderLayout.LINE_END);
        JPanel wrapper = this.componentsFactory.wrapToSlide(root);
        wrapper.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        return wrapper;
    }

    protected JPanel getBottomPanel() {
        JPanel root = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER));
        JButton selectButton = this.componentsFactory.getBorderedButton("Select");
        selectButton.setFont(this.componentsFactory.getFont(FontStyle.BOLD, 15f));
        JButton cancelButton = componentsFactory.getButton(
                FontStyle.BOLD,
                AppThemeColor.FRAME_RGB,
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                "Cancel",
                15f);
        selectButton.setPreferredSize(new Dimension(128, 26));
        cancelButton.setPreferredSize(new Dimension(128, 26));
        selectButton.addActionListener(action -> {
            this.callback.onAction((String) this.iconsList.getSelectedValue());
            this.setVisible(false);
        });
        cancelButton.addActionListener(action -> this.setVisible(false));
        root.add(selectButton);
        root.add(cancelButton);
        return root;
    }

    protected boolean isValidIconPath(String name) {
        return name != null && (name.endsWith(".png"));
    }
}
