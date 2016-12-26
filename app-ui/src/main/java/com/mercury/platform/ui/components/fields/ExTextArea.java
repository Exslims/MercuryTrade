package com.mercury.platform.ui.components.fields;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 16.12.2016.
 */
public class ExTextArea extends JTextArea {
    public ExTextArea() {
        init();
    }

    private void init() {
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        this.setMinimumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        this.setEditable(false);
    }



}
