package com.mercury.platform.ui.adr.components;


import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

public class AdrGroupFrame extends AbstractAdrComponentFrame<AdrTestGroup>{
    private JPanel header;
    public AdrGroupFrame(AdrTestGroup descriptor) {
        super(descriptor);
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.setPreferredSize(this.descriptor.getSize());
        header = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = this.componentsFactory.getTextLabel(this.descriptor.getTitle());
        header.setBackground(AppThemeColor.ADR_BG);
        header.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        header.add(label);
        this.add(header,BorderLayout.PAGE_START);
        this.pack();
    }

    @Override
    public void subscribe() {
        super.subscribe();
    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        this.setBackground(AppThemeColor.TRANSPARENT);
        header.setBackground(AppThemeColor.ADR_BG);
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
