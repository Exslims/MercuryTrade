package com.mercury.platform.ui.adr.components.panel.tree.main;

import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrNodePanel;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;


public class AdrIconNodePanel extends AdrNodePanel<AdrIconDescriptor> {
    public AdrIconNodePanel(AdrIconDescriptor descriptor, boolean inner) {
        super(descriptor,inner);
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
        this.add(this.adrComponentsFactory.getLeftComponentOperationsPanel(this.descriptor),BorderLayout.LINE_START);
        this.add(this.adrComponentsFactory.getRightComponentOperationsPanel(this.descriptor),BorderLayout.LINE_END);
    }
}
