package com.mercury.platform.ui.adr.components.panel.tree.dialog;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrTreeNodeRenderer;

import javax.swing.*;


public class AdrDialogTreeNodeRenderer implements AdrTreeNodeRenderer{
    @Override
    public JPanel getViewOf(AdrComponentDescriptor descriptor) {
        switch (descriptor.getType()){
            case ICON:{
                return new AdrDialogIconNodePanel((AdrIconDescriptor) descriptor);
            }
            case PROGRESS_BAR:{
                return new AdrDialogPBNodePanel((AdrProgressBarDescriptor) descriptor);
            }
            case GROUP: {
                return new AdrDialogGroupNodePanel((AdrGroupDescriptor) descriptor);
            }
        }
        return new JPanel();
    }
}
