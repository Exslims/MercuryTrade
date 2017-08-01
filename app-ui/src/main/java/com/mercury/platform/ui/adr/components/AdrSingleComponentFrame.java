package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.descriptor.adr.AdrDurationComponentDescriptor;
import com.mercury.platform.ui.adr.components.panel.AdrCellPanel;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import rx.Subscription;

import javax.swing.*;
import java.awt.*;


public class AdrSingleComponentFrame extends AbstractAdrComponentFrame<AdrDurationComponentDescriptor> {
    private AdrCellPanel component;
    private Subscription adrReloadSubscription;
    public AdrSingleComponentFrame(AdrDurationComponentDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void initialize() {
        super.initialize();

        JPanel root = this.componentsFactory.getTransparentPanel(new GridLayout(1,1));
        this.component = new AdrCellPanel(this.descriptor, this.componentsFactory);
        root.add(this.component);
        this.add(root,BorderLayout.CENTER);
        this.pack();
    }

    @Override
    public void subscribe() {
        super.subscribe();
        this.adrReloadSubscription = MercuryStoreUI.adrReloadSubject.subscribe(descriptor -> {
            if(descriptor.equals(this.descriptor)){
                this.setOpacity(this.descriptor.getOpacity());
                this.setLocation(descriptor.getLocation());
                this.setPreferredSize(this.descriptor.getSize());
                this.repaint();
                this.pack();
            }
        });
    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        this.component.enableSettings();
        this.component.addMouseListener(this.mouseListener);
        this.component.addMouseListener(this.mouseOverListener);
        this.component.addMouseMotionListener(this.motionListener);
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        this.component.disableSettings();
        this.component.removeMouseListener(this.mouseListener);
        this.component.removeMouseMotionListener(this.motionListener);
        this.component.removeMouseListener(this.mouseOverListener);
        this.pack();
        this.repaint();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.adrReloadSubscription.unsubscribe();
    }
}
