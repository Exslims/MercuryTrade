package com.home.clicker.ui.components;

import com.home.clicker.ui.components.fields.label.FontStyle;
import com.home.clicker.ui.components.fields.label.TextAlignment;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Константин on 23.12.2016.
 */
public class ComponentsFactory {
    private final static Logger log = Logger.getLogger(ComponentsFactory.class);

    /**
     * Get label with custom params
     * @param fontStyle path of exile font type
     * @param frColor foreground color
     * @param alignment font alignment
     * @param size font size
     * @param text initial text on label
     * @return JLabel object
     */
    public JLabel getTextLabel(FontStyle fontStyle, Color frColor, TextAlignment alignment, float size, String text){
        JLabel label = new JLabel(text);
        Font font = new Font("Tahoma", Font.BOLD, 16); //default
        try {
            switch (fontStyle) {
                case BOLD:
                    font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("font/Fontin-Bold.ttf"));
                    break;
                case ITALIC:
                    font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("font/Fontin-Italic.ttf"));
                    break;
                case REGULAR:
                    font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("font/Fontin-Regular.ttf"));
                    break;
                case SMALLCAPS:
                    font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("font/Fontin-SmallCaps.ttf"));
                    break;
            }
        }catch (Exception e){
            log.error(e);
        }
        label.setFont(font.deriveFont(size));
        label.setForeground(frColor);

        if(alignment != null) {
            switch (alignment) {
                case LEFTOP: {
                    label.setAlignmentX(Component.LEFT_ALIGNMENT);
                    label.setAlignmentY(Component.TOP_ALIGNMENT);
                }
                break;
                case RIGHTOP: {
                    label.setAlignmentX(Component.RIGHT_ALIGNMENT);
                    label.setAlignmentY(Component.TOP_ALIGNMENT);
                }
                break;
            }
        }
        return label;
    }

    /**
     * Get label with icon
     * @param iconPath icon path from maven resources
     * @param size icon size
     * @return JLabel object
     */
    public JLabel getIconLabel(String iconPath, int size){
        JLabel iconLabel = new JLabel();
        try {
            BufferedImage buttonIcon = ImageIO.read(getClass().getClassLoader().getResource(iconPath));
            BufferedImage icon = Scalr.resize(buttonIcon, size);
            iconLabel.setIcon(new ImageIcon(icon));
        } catch (IOException e) {
            log.error(e);
        }
        return iconLabel;
    }
}
