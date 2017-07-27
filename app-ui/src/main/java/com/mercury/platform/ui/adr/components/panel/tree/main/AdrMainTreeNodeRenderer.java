package com.mercury.platform.ui.adr.components.panel.tree.main;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrTreeNodeRenderer;
import com.mercury.platform.ui.components.ComponentsFactory;

import javax.swing.*;


public class AdrMainTreeNodeRenderer implements AdrTreeNodeRenderer {
    private ComponentsFactory componentsFactory = new ComponentsFactory();
    @Override
    public JPanel getViewOf(AdrComponentDescriptor descriptor, boolean inner) {
        switch (descriptor.getType()){
            case ICON:{
                return this.componentsFactory.wrapToAdrSlide(new AdrIconNodePanel((AdrIconDescriptor) descriptor,inner), 2, 4, 2, 4);
            }
            case PROGRESS_BAR:{
                return this.componentsFactory.wrapToAdrSlide(new AdrProgressBarNodePanel((AdrProgressBarDescriptor) descriptor,inner), 2, 4, 2, 4);
            }
            case GROUP: {
                return this.componentsFactory.wrapToAdrSlide(new AdrGroupNodePanel((AdrTrackerGroupDescriptor) descriptor), 2, 4, 2, 4);
            }
        }
        return new JPanel();
    }
}
