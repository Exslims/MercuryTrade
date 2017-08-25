package com.mercury.platform.ui.adr.components.panel;

import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.store.DestroySubscription;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;
import rx.Subscription;

import javax.swing.*;


public abstract class AdrComponentPanel<T extends AdrComponentDescriptor> extends JPanel implements ViewInit, AsSubscriber, DestroySubscription {
    @Getter
    protected T descriptor;
    protected ComponentsFactory componentsFactory;
    protected boolean inSettings;

    private Subscription adrReloadSubscription;
    private Subscription adrSelectSubscription;

    public AdrComponentPanel(T descriptor, ComponentsFactory componentsFactory) {
        this.descriptor = descriptor;
        this.componentsFactory = componentsFactory;
        this.subscribe();
        this.onViewInit();
    }

    @Override
    public void subscribe() {
        this.adrReloadSubscription = MercuryStoreUI.adrReloadSubject.subscribe(it -> {
            if (this.descriptor.equals(it) && this.inSettings) {
                this.onUpdate();
                this.onSelect();
            }
        });
        this.adrSelectSubscription = MercuryStoreUI.adrSelectSubject.subscribe(it -> {
            if (this.descriptor.equals(it)) {
                this.onSelect();
            } else {
                this.onUnSelect();
            }
        });
    }

    public void enableSettings() {
        this.inSettings = true;
    }

    public void disableSettings() {
        this.inSettings = false;
    }

    public abstract void onSelect();

    public abstract void onUnSelect();

    protected abstract void onUpdate();

    @Override
    public void onDestroy() {
        this.adrReloadSubscription.unsubscribe();
        this.adrSelectSubscription.unsubscribe();
    }
}
