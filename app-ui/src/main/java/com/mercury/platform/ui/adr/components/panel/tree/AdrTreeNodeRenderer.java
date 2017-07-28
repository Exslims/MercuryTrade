package com.mercury.platform.ui.adr.components.panel.tree;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;

import javax.swing.*;

public interface AdrTreeNodeRenderer {
    JPanel getViewOf(AdrTreeNode<AdrComponentDescriptor> node);
}
