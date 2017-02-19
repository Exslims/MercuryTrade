package com.mercury.platform.ui.components.panel.mesh;

import javax.swing.*;

public class ItemCell{
    private int x;
    private int y;
    private JLabel label;
    public ItemCell(int x, int y, JLabel label) {
        this.x = x;
        this.y = y;
        this.label = label;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public JLabel getLabel() {
        return label;
    }
}
