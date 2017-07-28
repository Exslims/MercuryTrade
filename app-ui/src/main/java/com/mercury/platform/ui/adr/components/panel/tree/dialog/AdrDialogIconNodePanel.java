package com.mercury.platform.ui.adr.components.panel.tree.dialog;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrNodePanel;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;


public class AdrDialogIconNodePanel extends AdrNodePanel<AdrIconDescriptor> {
    private JLabel iconLabel;

    public AdrDialogIconNodePanel(AdrTreeNode<AdrComponentDescriptor> treeNode) {
        super(treeNode);
        this.mouseListener.setProcessSelect(false);
    }

    @Override
    protected void update() {
        this.iconLabel.setText(this.descriptor.getTitle());
        this.iconLabel.setIcon(this.componentsFactory.getIcon("app/adr/icons/" + descriptor.getIconPath() + ".png",28));
    }

    @Override
    public void createUI() {
        this.iconLabel = this.componentsFactory.getIconLabel("app/adr/icons/" + descriptor.getIconPath() + ".png",28);
        this.iconLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
        this.iconLabel.setFont(componentsFactory.getFont(FontStyle.REGULAR, 16));
        this.iconLabel.setText(descriptor.getTitle());
        this.iconLabel.setPreferredSize(new Dimension(150, 28));
        this.iconLabel.setBorder(BorderFactory.createEmptyBorder());
        this.add(this.iconLabel,BorderLayout.CENTER);
        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 15, AppThemeColor.ADR_BG, TooltipConstants.ADR_REMOVE_BUTTON);
        this.add(removeButton,BorderLayout.LINE_END);
    }
}
