package com.mercury.platform.ui.components.datatable.renderer;

import javax.swing.*;

public interface MCellRenderer<T> {
    JComponent getComponent(T data);
}
