package com.mercury.platform.ui.components.panel.grid;

import javax.swing.*;

public class ItemCell{
    private int x;
    private int y;
    private JPanel cell;
    public ItemCell(int x, int y, JPanel cell) {
        this.x = x;
        this.y = y;
        this.cell = cell;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public JPanel getCell() {
        return cell;
    }
}
