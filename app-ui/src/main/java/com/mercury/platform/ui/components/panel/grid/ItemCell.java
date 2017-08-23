package com.mercury.platform.ui.components.panel.grid;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;

@Data
@AllArgsConstructor
public class ItemCell {
    private int x;
    private int y;
    private JPanel cell;
}
