package com.mercury.platform.ui.adr.components.panel;

import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;

import javax.swing.*;


public abstract class AdrComponentPanel<T extends AdrComponentDescriptor> extends JPanel implements HasUI,AsSubscriber {
    @Getter
    protected T descriptor;
    protected ComponentsFactory componentsFactory;
    protected boolean inSettings;
    public AdrComponentPanel(T descriptor, ComponentsFactory componentsFactory) {
        this.descriptor = descriptor;
        this.componentsFactory = componentsFactory;
        this.subscribe();
        this.createUI();
    }

    @Override
    public void subscribe() {
        MercuryStoreUI.adrReloadSubject.subscribe(it -> {
            if(this.descriptor.equals(it)){
                this.onUpdate();
                this.onSelect();
            }
        });
        MercuryStoreUI.adrSelectSubject.subscribe(it -> {
            if(this.descriptor.equals(it)){
                this.onSelect();
            }else {
                this.onUnSelect();
            }
        });
        MercuryStoreCore.hotKeySubject.subscribe(hotKey -> {
            if(this.descriptor.getHotKeyDescriptor() != null) {
                if (this.descriptor.getHotKeyDescriptor().equals(hotKey) && !this.inSettings) {
                    this.onHotKeyPressed();
                }
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
    protected abstract void onHotKeyPressed();
    protected abstract void onUpdate();
}
