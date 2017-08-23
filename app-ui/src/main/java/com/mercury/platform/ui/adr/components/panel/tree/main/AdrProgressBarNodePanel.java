package com.mercury.platform.ui.adr.components.panel.tree.main;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrNodePanel;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryTracker;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;


public class AdrProgressBarNodePanel extends AdrNodePanel<AdrProgressBarDescriptor> {
    private MercuryTracker tracker;

    public AdrProgressBarNodePanel(AdrTreeNode<AdrComponentDescriptor> treeNode) {
        super(treeNode);
    }

    @Override
    protected void update() {
        this.tracker.setMaximum((int) (this.descriptor.getDuration() * 1000));
        this.tracker.setValue((int) (this.descriptor.getDuration() * 1000) / 2);
        this.tracker.updateUI();
    }

    @Override
    public void onViewInit() {
        JPanel root = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER));
        root.setBackground(AppThemeColor.SLIDE_BG);
        this.tracker = new MercuryTracker(descriptor);
        this.tracker.setValue((int) ((descriptor.getDuration() / 2) * 1000));
        this.tracker.setFont(new ComponentsFactory().getFont(FontStyle.BOLD, 20));
        this.tracker.setPreferredSize(new Dimension(150, 36));
        this.tracker.setShowCase(true);
        this.tracker.setBackground(AppThemeColor.ADR_TEXT_ARE_BG);
        this.setPreferredSize(new Dimension(150, 48));
        root.add(this.tracker);
        this.add(root, BorderLayout.CENTER);
        this.add(this.adrComponentsFactory.getLeftComponentOperationsPanel(this.treeNode), BorderLayout.LINE_START);
        this.add(this.adrComponentsFactory
                .getRightComponentOperationsPanel(this.descriptor), BorderLayout.LINE_END);
    }
}
