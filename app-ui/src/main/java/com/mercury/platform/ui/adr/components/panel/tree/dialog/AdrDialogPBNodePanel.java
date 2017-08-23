package com.mercury.platform.ui.adr.components.panel.tree.dialog;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrNodePanel;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryTracker;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

public class AdrDialogPBNodePanel extends AdrNodePanel<AdrProgressBarDescriptor> {
    public AdrDialogPBNodePanel(AdrTreeNode<AdrComponentDescriptor> treeNode) {
        super(treeNode);
        this.mouseListener.setProcessSelect(false);
    }

    @Override
    protected void update() {

    }

    @Override
    public void onViewInit() {
        JPanel root = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER));
        root.setBackground(AppThemeColor.SLIDE_BG);
        MercuryTracker tracker = new MercuryTracker(descriptor);
        tracker.setValue((int) ((descriptor.getDuration() / 2) * 1000));
        tracker.setPreferredSize(new Dimension(180, 30));
        tracker.setShowCase(true);
        root.add(tracker);
        this.add(root, BorderLayout.CENTER);
    }
}
