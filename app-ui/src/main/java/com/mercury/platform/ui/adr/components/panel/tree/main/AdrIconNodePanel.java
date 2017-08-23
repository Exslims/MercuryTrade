package com.mercury.platform.ui.adr.components.panel.tree.main;

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


public class AdrIconNodePanel extends AdrNodePanel<AdrIconDescriptor> {
    private JLabel titleLabel;
    private MercuryTracker tracker;

    public AdrIconNodePanel(AdrTreeNode<AdrComponentDescriptor> treeNode) {
        super(treeNode);
    }


    @Override
    protected void update() {
        this.titleLabel.setText(this.descriptor.getTitle());
        this.tracker.updateUI();
    }

    @Override
    public void onViewInit() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(AppThemeColor.ADR_BG);
        this.tracker = new MercuryTracker(this.descriptor);
        this.tracker.setPreferredSize(new Dimension(48, 48));
        this.tracker.setShowCase(true);
        this.tracker.setValue(new Random().nextInt((int) (this.descriptor.getDuration() * 1000)));
        this.tracker.setFont(new ComponentsFactory().getFont(FontStyle.BOLD, 20));
        this.tracker.setBackground(AppThemeColor.ADR_TEXT_ARE_BG);

        this.titleLabel = this.componentsFactory.getTextLabel(this.descriptor.getTitle());
        this.titleLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
        this.titleLabel.setFont(componentsFactory.getFont(FontStyle.REGULAR, 16));
        this.titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        root.add(tracker, BorderLayout.LINE_START);
        root.add(this.titleLabel, BorderLayout.CENTER);
        this.add(root, BorderLayout.CENTER);
        this.add(this.adrComponentsFactory.getLeftComponentOperationsPanel(this.treeNode), BorderLayout.LINE_START);
        this.add(this.adrComponentsFactory
                .getRightComponentOperationsPanel(this.descriptor), BorderLayout.LINE_END);
    }
}
