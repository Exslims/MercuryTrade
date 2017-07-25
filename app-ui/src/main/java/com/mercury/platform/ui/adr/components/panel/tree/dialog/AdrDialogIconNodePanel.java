package com.mercury.platform.ui.adr.components.panel.tree.dialog;

import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrNodePanel;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;


public class AdrDialogIconNodePanel extends AdrNodePanel<AdrIconDescriptor> {
    public AdrDialogIconNodePanel(AdrIconDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public void createUI() {
        JLabel label = this.componentsFactory.getIconLabel("app/adr/icons/" + descriptor.getIconPath() + ".png",48);
        label.setForeground(AppThemeColor.TEXT_DEFAULT);
        label.setFont(componentsFactory.getFont(FontStyle.REGULAR, 16));
        label.setText(descriptor.getTitle());
        label.setPreferredSize(new Dimension(150, 48));
        label.setBorder(BorderFactory.createEmptyBorder());
        this.add(label,BorderLayout.CENTER);
        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 15, AppThemeColor.ADR_BG, TooltipConstants.ADR_REMOVE_BUTTON);
        this.add(removeButton,BorderLayout.LINE_END);
    }
}
