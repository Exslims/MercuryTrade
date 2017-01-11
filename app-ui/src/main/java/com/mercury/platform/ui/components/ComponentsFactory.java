package com.mercury.platform.ui.components;

import com.mercury.platform.ui.components.fields.MercuryComboBoxUI;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Factory for each element which uses in application
 */
public class ComponentsFactory {
    private final static Logger log = Logger.getLogger(ComponentsFactory.class);

    private static class ComponentsFactoryHolder {
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
        if(isAscii(text)){
            button.setFont(getSelectedFont(fontStyle).deriveFont(fontSize));
        }else {
            button.setFont(DEFAULT_FONT.deriveFont(fontSize));
        }
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
    public JButton getIconButton(String iconPath, int iconSize, Color background){
        JButton button = new JButton(""){
            @Override
            protected void paintBorder(Graphics g) {
                if(!this.getModel().isPressed()) {
                    super.paintBorder(g);
                }
            }
        };
        button.setBackground(background);
        button.setFocusPainted(false);
        button.addChangeListener(e->{
            if(!button.getModel().isPressed()){
                button.setBackground(background);
            }
        });
//        button.setContentAreaFilled(false);
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
        JButton iconButton = getIconButton(iconPath, iconSize, AppThemeColor.FRAME_1);
        iconButton.setBorder(BorderFactory.createLineBorder(AppThemeColor.BUTTON, 2));
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
        JButton iconButton = getIconButton(iconPath, iconSize, AppThemeColor.FRAME_1);
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
     * @param text initial text on font
     * @return JLabel object
     */
    public JLabel getTextLabel(FontStyle fontStyle, Color frColor, TextAlignment alignment, float size, String text){
        JLabel label = new JLabel(text);
        if(isAscii(text)){
            label.setFont(getSelectedFont(fontStyle).deriveFont(size));
        }else {
            label.setFont(DEFAULT_FONT.deriveFont(size));
        }
        label.setForeground(frColor);
        Border border = label.getBorder();
        label.setBorder(new CompoundBorder(border,new EmptyBorder(0,5,0,5)));

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
                    label.setAlignmentX(Component.CENTER_ALIGNMENT);
                    label.setAlignmentY(Component.TOP_ALIGNMENT);
                }
                break;
            }
        }
        return label;
    }

    /**
     * Get default label
     * @param text font text
     * @return JLabel object
     */
    public JLabel getTextLabel(String text){
        return getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT,TextAlignment.LEFTOP,15f,text);
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

    public JTextField getTextField(String text){
        JTextField textField = getTextField(text,null,16);
        textField.setFont(DEFAULT_FONT);
        return textField;
    }
    public JTextField getTextField(String text, FontStyle style, float fontSize){
        JTextField textField = new JTextField(text);
        if(style != null) {
            textField.setFont(getSelectedFont(style).deriveFont(fontSize));
        }
        textField.setForeground(AppThemeColor.TEXT_DEFAULT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TEXT_DEFAULT,1),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,3)
        ));
        textField.setBackground(AppThemeColor.HEADER);
        return textField;
    }
    public JComboBox getComboBox(String[] childs){
        JComboBox comboBox = new JComboBox(childs);
        comboBox.setBackground(AppThemeColor.HEADER);
        comboBox.setForeground(AppThemeColor.TEXT_DEFAULT);
        comboBox.setFont(BOLD_FONT.deriveFont(16f));
        comboBox.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER,1));
        comboBox.setUI(MercuryComboBoxUI.createUI(comboBox));
        return comboBox;
    }
    public JSlider getSlider(int min, int max, int value){
        JSlider slider = new JSlider(JSlider.HORIZONTAL,min,max,value);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setBackground(AppThemeColor.TRANSPARENT);
        return slider;
    }
    public JPanel getTransparentPanel(LayoutManager layout){
        JPanel panel = new JPanel(layout);
        panel.setBackground(AppThemeColor.TRANSPARENT);
        return panel;
    }

    public JPanel getBorderedTransparentPanel(Border border, LayoutManager layout){
        JPanel panel = new JPanel(layout);
        panel.setBackground(AppThemeColor.TRANSPARENT);
        panel.setBorder(border);
        return panel;
    }
    public ImageIcon getIcon(String iconPath, int size){
        BufferedImage icon = null;
        try {
            BufferedImage buttonIcon = ImageIO.read(getClass().getClassLoader().getResource(iconPath));
            icon = Scalr.resize(buttonIcon, size);
        } catch (IOException e) {
            log.error(e);
        }
        return new ImageIcon(icon);
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
    private boolean isAscii(CharSequence sequence){
        if(sequence != null) {
            for (int i = sequence.length() - 1; i >= 0; i--) {
                if (!matches(sequence.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean matches(char c) {
        return c <= '\u007f';
    }
}
