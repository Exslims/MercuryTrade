package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.ui.adr.components.panel.AdrComponentPanel;
import com.mercury.platform.ui.adr.components.panel.AdrTrackerGroupPanel;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.NonNull;
import rx.Subscription;

import javax.swing.*;


public class AdrTrackerGroupFrame extends AbstractAdrComponentFrame<AdrTrackerGroupDescriptor> {
    private AdrTrackerGroupPanel trackerGroupPanel;
    private Subscription adrReloadSubscription;

    public AdrTrackerGroupFrame(@NonNull AdrTrackerGroupDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public void setPanel(AdrComponentPanel panel) {
        this.trackerGroupPanel = new AdrTrackerGroupPanel(this.descriptor, this.componentsFactory);
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.add(this.trackerGroupPanel);
        this.pack();
    }

    @Override
    public void subscribe() {
        super.subscribe();
        this.adrReloadSubscription = MercuryStoreUI.adrReloadSubject.subscribe(descriptor -> {
            if (descriptor.equals(this.descriptor)) {
                this.setLocation(descriptor.getLocation());
                this.setOpacity(this.descriptor.getOpacity());
                this.repaint();
                this.pack();
            }
        });
    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        this.trackerGroupPanel.enableSettings();
        this.trackerGroupPanel.addMouseListener(this.mouseListener);
        this.trackerGroupPanel.addMouseListener(this.mouseOverListener);
        this.trackerGroupPanel.addMouseMotionListener(this.motionListener);
        this.pack();
        this.repaint();
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        this.trackerGroupPanel.disableSettings();
        this.trackerGroupPanel.removeMouseListener(this.mouseListener);
        this.trackerGroupPanel.removeMouseListener(this.mouseOverListener);
        this.trackerGroupPanel.removeMouseMotionListener(this.motionListener);
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        this.pack();
        this.repaint();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.adrReloadSubscription.unsubscribe();
        this.trackerGroupPanel.onDestroy();
    }

    @Override
    public void onViewInit() {

    }
}
