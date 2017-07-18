package com.mercury.platform.ui.adr.components.panel.page;

import com.mercury.platform.shared.config.descriptor.adr.AdrGroupContentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.adr.HotKeyManager;
import com.mercury.platform.ui.components.fields.font.FontStyle;

import javax.swing.*;
import java.awt.*;


public class AdrIconPagePanel extends AdrPagePanel<AdrIconDescriptor> {
    private HotKeyManager hotKeyManager = new HotKeyManager();
    @Override
    protected void init() {
        JLabel titleLabel = this.componentsFactory.getTextLabel("HotKey:");
        JLabel groupTitleLabel = this.componentsFactory.getTextLabel("Group type:");
        String sizeText = "Icons size:";
        JLabel sizeLabel = this.componentsFactory.getTextLabel(sizeText);
        JLabel opacityLabel = this.componentsFactory.getTextLabel("Opacity:");
        JLabel groupDirectionLabel = this.componentsFactory.getTextLabel("Direction:");
        JLabel groupScaleLabel = this.componentsFactory.getTextLabel("Scale:");

        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 2,0,6));
        root.setBorder(BorderFactory.createEmptyBorder(6,0,0,4));
        root.add(titleLabel);
        root.add(this.hotKeyManager.getHotKeyButton(this.payload.getHotKeyDescriptor()));
        this.add(root,BorderLayout.PAGE_START);
    }
}
