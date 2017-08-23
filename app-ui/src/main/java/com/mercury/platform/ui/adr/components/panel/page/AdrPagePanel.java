package com.mercury.platform.ui.adr.components.panel.page;

import com.mercury.platform.ui.adr.components.AdrComponentsFactory;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public abstract class AdrPagePanel<T> extends JPanel {
    @Getter
    @Setter
    protected boolean fromGroup;
    protected T payload;
    protected ComponentsFactory componentsFactory = new ComponentsFactory();
    protected boolean advancedExpanded;
    protected AdrComponentsFactory adrComponentsFactory = new AdrComponentsFactory(this.componentsFactory);

    public AdrPagePanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.FRAME);
    }

    protected abstract void init();

    public void setPayload(T payload) {
        this.adrComponentsFactory = new AdrComponentsFactory(this.componentsFactory);
        this.payload = payload;
        this.removeAll();
        this.init();
    }
}
