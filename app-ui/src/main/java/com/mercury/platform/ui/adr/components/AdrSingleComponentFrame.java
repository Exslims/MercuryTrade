package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.adr.components.panel.AdrComponentPanel;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;
import rx.Subscription;

import javax.swing.*;


public class AdrSingleComponentFrame extends AbstractAdrComponentFrame<AdrComponentDescriptor> {
    @Getter
    private AdrComponentPanel component;
    private Subscription adrReloadSubscription;

    public AdrSingleComponentFrame(AdrComponentDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public void setPanel(AdrComponentPanel panel) {
        this.component = panel;
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.add(this.component);
        this.pack();
    }

    @Override
    public void subscribe() {
        super.subscribe();
        this.adrReloadSubscription = MercuryStoreUI.adrReloadSubject.subscribe(descriptor -> {
            if (descriptor.equals(this.descriptor)) {
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
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        this.pack();
        this.repaint();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.adrReloadSubscription.unsubscribe();
        this.component.onDestroy();
        this.setVisible(false);
    }

    @Override
    public void onViewInit() {

    }
}
