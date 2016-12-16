package com.home.clicker.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 15.12.2016.
 */
public class ExLabel extends JLabel {
    public ExLabel(Icon image) {
        super(image);
        init();
    }

    public ExLabel(String text) {
        super(text);
        init();
    }

    public ExLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        init();
    }

    private void init() {
//        Border border = this.getBorder();
//        this.setBorder(new CompoundBorder(border,new EmptyBorder(10,5,10,10)));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.setAlignmentY(Component.TOP_ALIGNMENT);
        this.setFont(new Font("Tahoma", Font.BOLD, 15));
    }
}
