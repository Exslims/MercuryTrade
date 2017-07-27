package com.mercury.platform.ui.adr.components.panel.tree.main;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrTreeNodeRenderer;

import javax.swing.*;


public class AdrMainTreeNodeRenderer implements AdrTreeNodeRenderer {
    @Override
    public JPanel getViewOf(AdrComponentDescriptor descriptor, boolean inner) {
        switch (descriptor.getType()){
            case ICON:{
                return new AdrIconNodePanel((AdrIconDescriptor) descriptor,inner);
            }
            case PROGRESS_BAR:{
                return new AdrProgressBarNodePanel((AdrProgressBarDescriptor) descriptor,inner);
            }
            case GROUP: {
                return new AdrGroupNodePanel((AdrTrackerGroupDescriptor) descriptor);
            }
        }
        return new JPanel();
    }
}
