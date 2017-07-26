package com.mercury.platform.ui.adr.components.panel;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;


public abstract class AdrComponentPanel<T extends AdrComponentDescriptor> extends JPanel implements HasUI {
    protected T descriptor;
    protected ComponentsFactory componentsFactory;
    protected boolean inSettings;
    AdrComponentPanel(T descriptor, ComponentsFactory componentsFactory) {
        this.descriptor = descriptor;
        this.componentsFactory = componentsFactory;

        MercuryStoreUI.adrReloadSubject.subscribe(it -> {
            if(this.descriptor.equals(it)){
                this.update();
            }
        });
        MercuryStoreUI.adrSelectSubject.subscribe(it -> {
            if(descriptor.equals(it)){
                this.onSelect();
            }else {
                this.onUnSelect();
            }
        });
        MercuryStoreCore.hotKeySubject.subscribe(hotKey -> {
            if(this.descriptor.getHotKeyDescriptor().equals(hotKey) && !this.inSettings){
                this.onHotKeyPressed();
            }
        });
    }
    public void enableSettings() {
        this.inSettings = true;
    }
    public void disableSettings() {
        this.inSettings = false;
    }
    protected abstract void onSelect();
    protected abstract void onUnSelect();
    protected abstract void onHotKeyPressed();
    public void update(){
        this.removeAll();
        this.createUI();
        this.onSelect();
    }
}
