package com.mercury.platform.ui.adr.components.panel.tree.dialog;


import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrNodePanel;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryTracker;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;

public class AdrDialogPBNodePanel extends AdrNodePanel<AdrProgressBarDescriptor>{
    public AdrDialogPBNodePanel(AdrProgressBarDescriptor descriptor) {
        super(descriptor);
    }
    @Override
    public void createUI() {
        JPanel root = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER));
        root.setBackground(AppThemeColor.SLIDE_BG);
        MercuryTracker tracker = new MercuryTracker(descriptor);
        tracker.setValue((int) ((descriptor.getDuration()/2) * 1000));
        tracker.setStringPainted(false);
        tracker.setPreferredSize(new Dimension(150, 26));
        this.setPreferredSize(new Dimension(150, 36));
        root.add(tracker);
        this.add(root,BorderLayout.CENTER);
        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 15, AppThemeColor.FRAME, TooltipConstants.ADR_REMOVE_BUTTON);
        removeButton.setBackground(AppThemeColor.ADR_BG);
        this.add(removeButton,BorderLayout.LINE_END);
    }
}
