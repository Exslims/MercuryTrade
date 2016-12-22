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
 * Factory for each element which uses in application
 */
public class ComponentsFactory {
    private final static Logger log = Logger.getLogger(ComponentsFactory.class);

    public static class ComponentsFactoryHolder {
        static final ComponentsFactory HOLDER_INSTANCE = new ComponentsFactory();
    }
    public static ComponentsFactory INSTANCE = ComponentsFactoryHolder.HOLDER_INSTANCE;

    private Font BOLD_FONT;
    private Font ITALIC_FONT;
    private Font REGULAR_FONT;
    private Font SMALLCAPS_FONT;
    private Font DEFAULT_FONT;

    public ComponentsFactory() {
        loadFonts();
    }

    /**
     * Loading all application fonts
     */
    private void loadFonts(){
        try {
            BOLD_FONT = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("font/Fontin-Bold.ttf"));
            ITALIC_FONT = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("font/Fontin-Italic.ttf"));
            REGULAR_FONT = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("font/Fontin-Regular.ttf"));
            SMALLCAPS_FONT = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("font/Fontin-SmallCaps.ttf"));
            DEFAULT_FONT = new Font("Tahoma", Font.BOLD, 16);

        } catch (Exception e) {
            log.error(e);
        }
    }

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
        switch (fontStyle) {
            case BOLD:
                label.setFont(BOLD_FONT.deriveFont(size));
                break;
            case ITALIC:
                label.setFont(ITALIC_FONT.deriveFont(size));
                break;
            case REGULAR:
                label.setFont(REGULAR_FONT.deriveFont(size));
                break;
            case SMALLCAPS:
                label.setFont(SMALLCAPS_FONT.deriveFont(size));
                break;
            default:
                label.setFont(DEFAULT_FONT.deriveFont(size));
        }
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
