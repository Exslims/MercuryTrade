package com.mercury.platform.ui.adr.components.panel.tree.dialog;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrNodePanel;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryTracker;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class AdrDialogIconNodePanel extends AdrNodePanel<AdrIconDescriptor> {
    public AdrDialogIconNodePanel(AdrTreeNode<AdrComponentDescriptor> treeNode) {
        super(treeNode);
        this.mouseListener.setProcessSelect(false);
    }

    @Override
    protected void update() {
    }

    @Override
    public void onViewInit() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(AppThemeColor.ADR_BG);
        MercuryTracker tracker = new MercuryTracker(this.descriptor);
        tracker.setPreferredSize(new Dimension(48, 48));
        tracker.setShowCase(true);
        tracker.setValue(new Random().nextInt((int) (this.descriptor.getDuration() * 1000)));
        tracker.setFont(new ComponentsFactory().getFont(FontStyle.BOLD, 20));
        tracker.setBackground(AppThemeColor.ADR_TEXT_ARE_BG);

        JLabel titleLabel = this.componentsFactory.getTextLabel(this.descriptor.getTitle());
        titleLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
        titleLabel.setFont(componentsFactory.getFont(FontStyle.REGULAR, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        root.add(tracker, BorderLayout.LINE_START);
        root.add(titleLabel, BorderLayout.CENTER);
        this.add(root, BorderLayout.CENTER);
    }
}
