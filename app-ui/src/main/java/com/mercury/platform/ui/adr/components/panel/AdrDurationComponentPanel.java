package com.mercury.platform.ui.adr.components.panel;

import com.mercury.platform.shared.config.descriptor.adr.AdrDurationComponentDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import rx.Subscription;

import javax.swing.*;


public abstract class AdrDurationComponentPanel<T extends AdrDurationComponentDescriptor> extends AdrComponentPanel<T> {
    private Subscription adrHotKeySubscription;

    public AdrDurationComponentPanel(T descriptor, ComponentsFactory componentsFactory) {
        super(descriptor, componentsFactory);
    }

    @Override
    public void subscribe() {
        super.subscribe();
        this.adrHotKeySubscription = MercuryStoreCore.hotKeySubject.subscribe(hotKey -> {
            SwingUtilities.invokeLater(() -> {
                if (this.descriptor.getHotKeyDescriptor() != null) {
                    if (this.descriptor.getHotKeyDescriptor().equals(hotKey) && !this.inSettings) {
                        this.onHotKeyPressed();
                    }
                }
            });
        });
    }

    protected abstract void onHotKeyPressed();

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.adrHotKeySubscription.unsubscribe();
    }
}
