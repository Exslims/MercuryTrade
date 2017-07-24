package com.mercury.platform.ui.adr.components.panel.tree;

import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;


public class AdrIconNodePanel extends AdrNodePanel<AdrIconDescriptor>{
    public AdrIconNodePanel(AdrIconDescriptor descriptor) {
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
        this.add(this.getRightPanel(),BorderLayout.LINE_END);
    }
    private JPanel getRightPanel() {
        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 15, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_REMOVE_BUTTON);
        JButton exportButton = this.componentsFactory.getIconButton("app/adr/export_node.png", 15, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_EXPORT_BUTTON);

        JPanel buttonsPanel = this.componentsFactory.getJPanel(new GridLayout(2, 1));
        buttonsPanel.setBackground(AppThemeColor.SLIDE_BG);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(exportButton);
        return buttonsPanel;
    }
}
