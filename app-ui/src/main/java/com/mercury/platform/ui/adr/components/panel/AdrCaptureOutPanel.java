package com.mercury.platform.ui.adr.components.panel;

import com.mercury.platform.shared.config.descriptor.adr.AdrCaptureDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;


public class AdrCaptureOutPanel extends AdrComponentPanel<AdrCaptureDescriptor> {
    public AdrCaptureOutPanel(AdrCaptureDescriptor descriptor, ComponentsFactory componentsFactory) {
        super(descriptor, componentsFactory);
    }

    @Override
    public void onViewInit() {
        this.setLayout(new GridLayout(1, 1));
        this.setPreferredSize(this.descriptor.getCaptureSize());
        this.setBackground(AppThemeColor.ADR_CAPTURE_BG);
        this.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_GREEN, 4));
        MercuryStoreUI.adrRepaintSubject.onNext(true);
    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        this.setVisible(true);
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        this.setVisible(false);
    }

    @Override
    public void onSelect() {

    }

    @Override
    public void onUnSelect() {

    }

    @Override
    protected void onUpdate() {
        this.setVisible(this.descriptor.isVisible());
    }
}
