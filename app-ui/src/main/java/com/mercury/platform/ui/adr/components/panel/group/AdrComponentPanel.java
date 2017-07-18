package com.mercury.platform.ui.adr.components.panel.group;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;


public abstract class AdrComponentPanel<T extends AdrComponentDescriptor> extends JPanel implements HasUI {
    protected T descriptor;
    protected ComponentsFactory componentsFactory;
    private boolean inSettings;
    AdrComponentPanel(T descriptor, ComponentsFactory componentsFactory) {
        this.descriptor = descriptor;
        this.componentsFactory = componentsFactory;

        MercuryStoreUI.adrReloadSubject.subscribe(it -> {
            if(it.equals(this.descriptor)){
                this.update();
            }
        });
        MercuryStoreCore.hotKeySubject.subscribe(hotKey -> {
//            if(this.descriptor.getHotKeyDescriptor().equals(hotKey) && !this.inSettings){
//                this.onHotKeyPressed();
//            }
        });
    }
    public void enableSettings() {
        this.inSettings = true;
    }
    public void disableSettings() {
        this.inSettings = false;
    }
    protected abstract void onHotKeyPressed();
    private void update(){
        this.removeAll();
        this.createUI();
    }

}
