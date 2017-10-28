package com.mercury.platform.ui.components.datatable.renderer;

import java.awt.*;

public interface MCellRenderer<T> {
    Component getComponent(T data);
}
