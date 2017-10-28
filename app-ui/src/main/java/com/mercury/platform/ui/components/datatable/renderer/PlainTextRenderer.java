package com.mercury.platform.ui.components.datatable.renderer;

import javax.swing.*;
import java.awt.*;

public class PlainTextRenderer implements MCellRenderer<String> {
    @Override
    public Component getComponent(String data) {
        return new JLabel(data);
    }
}
