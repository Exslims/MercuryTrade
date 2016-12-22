package com.home.clicker.ui.components.fields.label;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("font/Fontin-Bold.ttf"));
            this.setFont(font.deriveFont(18f));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
