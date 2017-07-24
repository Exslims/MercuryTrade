package com.mercury.platform.ui.adr.components.panel.tree;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;


public abstract class AdrNodePanel<D extends AdrComponentDescriptor> extends JPanel implements HasUI{
    @Getter
    protected D descriptor;
    protected ComponentsFactory componentsFactory = new ComponentsFactory();

    public AdrNodePanel(D descriptor) {
        this.descriptor = descriptor;
        this.addMouseListener(new AdrMouseOverListener(this));
        MercuryStoreUI.adrReloadSubject.subscribe(source -> {
            if(this.descriptor.equals(source)){
                this.update();
            }
        });
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.SLIDE_BG);
        this.setBorder(BorderFactory.createLineBorder(AppThemeColor.MSG_HEADER_BORDER));
        this.createUI();
    }
    private void update() {
        this.removeAll();
        this.createUI();
    }
}
