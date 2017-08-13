package com.mercury.platform.ui.components.panel.settings;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;

@Data
@AllArgsConstructor
public class MenuEntry {
    private String text;
    private MenuAction action;
    private ImageIcon imageIcon;
}
