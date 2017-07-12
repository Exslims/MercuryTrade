package com.mercury.platform.ui.adr.components.panel;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;

import java.awt.*;

public class AdrMainPagePanel extends AdrPagePanel<AdrComponentDescriptor> {
    public AdrMainPagePanel() {
        super();
    }

    @Override
    protected void init() {
        if(this.payload instanceof AdrGroupDescriptor){
            this.add(this.componentsFactory.getTextLabel(this.payload.getTitle() + ">"), BorderLayout.PAGE_START);
        }
        this.add(this.componentsFactory.getTextLabel("test"),BorderLayout.CENTER);
    }
}
