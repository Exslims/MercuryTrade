package com.mercury.platform.ui.adr.components.panel.tree.dialog;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrTreeNodeRenderer;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;

import javax.swing.*;


public class AdrDialogTreeNodeRenderer implements AdrTreeNodeRenderer{
    @Override
    public JPanel getViewOf(AdrTreeNode<AdrComponentDescriptor> node) {
        switch (node.getData().getType()){
            case ICON:{
                return new AdrDialogIconNodePanel(node);
            }
            case PROGRESS_BAR:{
                return new AdrDialogPBNodePanel(node);
            }
            case TRACKER_GROUP: {
                return new AdrDialogGroupNodePanel(node);
            }
        }
        return new JPanel();
    }
}
