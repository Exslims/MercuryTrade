package com.mercury.platform.ui.adr.components.panel.tree.main;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrTreeNodeRenderer;

import javax.swing.*;


public class AdrMainTreeNodeRenderer implements AdrTreeNodeRenderer {
    @Override
    public JPanel getViewOf(AdrComponentDescriptor descriptor) {
        switch (descriptor.getType()){
            case ICON:{
                return new AdrIconNodePanel((AdrIconDescriptor) descriptor);
            }
            case PROGRESS_BAR:{
                return new AdrProgressBarNodePanel((AdrProgressBarDescriptor) descriptor);
            }
            case GROUP: {
                return new AdrGroupNodePanel((AdrGroupDescriptor) descriptor);
            }
        }
        return new JPanel();
    }
}
