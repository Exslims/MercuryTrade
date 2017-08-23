package com.mercury.platform.ui.adr.components.panel.tree.dialog;

import com.mercury.platform.shared.config.descriptor.adr.AdrCaptureDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrNodePanel;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;


public class AdrDialogCaptureNodePanel extends AdrNodePanel<AdrCaptureDescriptor> {
    private JLabel titleLabel;

    public AdrDialogCaptureNodePanel(AdrTreeNode<AdrComponentDescriptor> treeNode) {
        super(treeNode);
        this.mouseListener.setProcessSelect(false);
    }

    @Override
    public void onViewInit() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setPreferredSize(new Dimension(180, 30));
        root.setBackground(AppThemeColor.ADR_BG);
        this.titleLabel = this.componentsFactory.getTextLabel(this.descriptor.getTitle());
        this.titleLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
        this.titleLabel.setFont(componentsFactory.getFont(FontStyle.REGULAR, 16));
        this.titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        JLabel iconLabel = this.componentsFactory.getIconLabel("app/adr/capture_icon.png", 44);

        root.add(iconLabel, BorderLayout.LINE_START);
        root.add(titleLabel, BorderLayout.CENTER);
        this.add(root, BorderLayout.CENTER);
    }

    @Override
    protected void update() {
        this.titleLabel.setText(this.descriptor.getTitle());
    }
}
