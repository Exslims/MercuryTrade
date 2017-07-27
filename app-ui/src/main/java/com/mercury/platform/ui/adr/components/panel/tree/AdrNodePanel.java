package com.mercury.platform.ui.adr.components.panel.tree;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.adr.components.AdrComponentsFactory;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;


public abstract class AdrNodePanel<D extends AdrComponentDescriptor> extends JPanel implements HasUI {
    @Getter
    protected D descriptor;
    protected ComponentsFactory componentsFactory = new ComponentsFactory();
    protected AdrComponentsFactory adrComponentsFactory = new AdrComponentsFactory(this.componentsFactory);
    protected AdrMouseOverListener mouseListener;
    protected boolean inner;

    public AdrNodePanel(D descriptor) {
        this(descriptor, false);
    }

    public AdrNodePanel(D descriptor, boolean inner) {
        this.descriptor = descriptor;
        this.inner = inner;
        this.mouseListener = (new AdrMouseOverListener<>(this, descriptor, inner));
        this.addMouseListener(this.mouseListener);
        MercuryStoreUI.adrReloadSubject.subscribe(source -> {
            if (this.descriptor.equals(source)) {
                this.update();
            }
        });
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.SLIDE_BG);
        this.setBorder(BorderFactory.createLineBorder(AppThemeColor.MSG_HEADER_BORDER));
        this.createUI();
    }
    protected abstract void update();
}
