package com.mercury.platform.ui.adr.components.panel.tree;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;

import javax.swing.*;

public interface AdrTreeNodeRenderer {
    JPanel getViewOf(AdrComponentDescriptor descriptor);
}
