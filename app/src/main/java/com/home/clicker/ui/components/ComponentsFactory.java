package com.home.clicker.ui.components;

import com.home.clicker.ui.components.fields.label.FontStyle;
import com.home.clicker.ui.components.fields.label.TextAlignment;
import com.home.clicker.ui.misc.AppThemeColor;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
     * Get button with custom params
     * @param fontStyle path of exile font type
     * @param background button background
     * @param border button border
     * @param text default text
     * @param fontSize font size
     * @return JButton object
     */
    public JButton getButton(FontStyle fontStyle, Color background, Border border, String text, float fontSize){
        JButton button = new JButton(text){
            @Override
            protected void paintBorder(Graphics g) {
                if(!this.getModel().isPressed()) {
                    super.paintBorder(g);
                }
            }
        };
        button.setBackground(background);
        button.setForeground(AppThemeColor.TEXT_DEFAULT);
        button.setFocusPainted(false);
        button.setFont(getSelectedFont(fontStyle).deriveFont(fontSize));
        button.setBorder(border);
        button.addChangeListener(e->{
            if(!button.getModel().isPressed()){
                button.setBackground(background);
            }
        });
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setEnabled(false);
                Timer timer = new Timer(1000,null);
                timer.addActionListener(event -> {
                    button.setEnabled(true);
                    timer.stop();
                });
                timer.start();
            }
        });

        return button;
    }

    /**
     * Get button with default properties
     * @param text text on button
     * @return Default app button
     */
    public JButton getButton(String text){
        CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 1),
                BorderFactory.createLineBorder(AppThemeColor.BUTTON, 3)
        );

        return getButton(FontStyle.BOLD, AppThemeColor.BUTTON, compoundBorder, text, 14f);
    }

    /**
     * Get bordered button with default properties.
     * @param text text on button
     * @return Default bordered app button
     */
    public JButton getBorderedButton(String text){
        CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1),
                BorderFactory.createLineBorder(AppThemeColor.BUTTON, 3)
        );
        return getButton(FontStyle.BOLD, AppThemeColor.BUTTON, compoundBorder, text, 14f);
    }

    /**
     * Get button with icon
     * @param iconPath icon path from maven resources
     * @param iconSize icon size
     * @return JButton object with icon
     */
    public JButton getIconButton(String iconPath, int iconSize){
        JButton button = new JButton(""){
            @Override
            protected void paintBorder(Graphics g) {
                if(!this.getModel().isPressed()) {
                    super.paintBorder(g);
                }
            }
        };
        button.setBackground(AppThemeColor.TRANSPARENT);
        button.setFocusPainted(false);
        button.addChangeListener(e->{
            if(!button.getModel().isPressed()){
                button.setBackground(AppThemeColor.TRANSPARENT);
            }
        });
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setEnabled(false);
                Timer timer = new Timer(1000,null);
                timer.addActionListener(event -> {
                    button.setEnabled(true);
                    timer.stop();
                });
                timer.start();
            }
        });
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,4));
        button.setVerticalAlignment(SwingConstants.CENTER);
        BufferedImage icon = null;
        try {
            BufferedImage buttonIcon = ImageIO.read(getClass().getClassLoader().getResource(iconPath));
            icon = Scalr.resize(buttonIcon, iconSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(icon != null){
            button.setIcon(new ImageIcon(icon));
        }
        return button;
    }

    /**
     * Get bordered default button with icon
     * @param iconPath icon path from maven resources
     * @param iconSize icon size
     * @return bordered JButton with icon
     */
    public JButton getBorderedIconButton(String iconPath, int iconSize){
        CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1),
                BorderFactory.createLineBorder(AppThemeColor.BUTTON, 2)
        );
        JButton iconButton = getIconButton(iconPath, iconSize);
        iconButton.setBorder(compoundBorder);
        return iconButton;
    }

    /**
     * Get icon button with custom size
     * @param iconPath icon path from maven resources
     * @param iconSize icon size
     * @param buttonSize button size (its only preferred)
     * @return JButton with icon
     */
    public JButton getIconButton(String iconPath, int iconSize, Dimension buttonSize){
        JButton iconButton = getIconButton(iconPath, iconSize);
        iconButton.setPreferredSize(buttonSize);
        iconButton.setSize(buttonSize);
        return iconButton;
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
        label.setFont(getSelectedFont(fontStyle).deriveFont(size));
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
                case CENTER:{
                    label.setHorizontalAlignment(SwingConstants.CENTER);
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
     * @return JLabel object with icon
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

    private Font getSelectedFont(FontStyle fontStyle){
        switch (fontStyle) {
            case BOLD:
                return BOLD_FONT;
            case ITALIC:
                return ITALIC_FONT;
            case REGULAR:
                return REGULAR_FONT;
            case SMALLCAPS:
                return SMALLCAPS_FONT;
        }
        return DEFAULT_FONT;
    }
}
