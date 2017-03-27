package com.mercury.platform.ui.components;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ButtonPressedEvent;
import com.mercury.platform.shared.events.custom.HideTooltipEvent;
import com.mercury.platform.ui.misc.event.ShowTooltipEvent;
import com.mercury.platform.ui.components.fields.style.MercuryComboBoxUI;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.misc.ToggleCallback;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.StringUtils;
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
public class ComponentsFactory{
    private final static Logger log = Logger.getLogger(ComponentsFactory.class);

    private Font BOLD_FONT;
    private Font ITALIC_FONT;
    private Font REGULAR_FONT;
    private Font SMALLCAPS_FONT;
    private Font DEFAULT_FONT;
    private float scale = 1f;

    public ComponentsFactory() {
        loadFonts();
        this.scale = ConfigManager.INSTANCE.getScaleData().get("other");
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
            DEFAULT_FONT = new Font("Tahoma", Font.BOLD, (int)(scale*16));

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
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    EventRouter.CORE.fireEvent(new ButtonPressedEvent());
                }
            }
        });
        if(isAscii(text)){
            button.setFont(getSelectedFont(fontStyle).deriveFont(scale*fontSize));
        }else {
            button.setFont(DEFAULT_FONT.deriveFont(scale*fontSize));
        }
        button.setBorder(border);
        button.addChangeListener(e->{
            if(!button.getModel().isPressed()){
                button.setBackground(button.getBackground());
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

        return getButton(FontStyle.BOLD, AppThemeColor.BUTTON, compoundBorder, text, scale*14f);
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
        return getButton(FontStyle.BOLD, AppThemeColor.BUTTON, compoundBorder, text, scale*14f);
    }
    public JButton getBorderedButton(String text, float fontSize){
        CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1),
                BorderFactory.createLineBorder(AppThemeColor.BUTTON, 3)
        );
        return getButton(FontStyle.BOLD, AppThemeColor.BUTTON, compoundBorder, text, scale*fontSize);
    }

    public Component setUpToggleCallbacks(Component button,ToggleCallback firstState, ToggleCallback secondState, boolean initialState){
        button.addMouseListener(new MouseAdapter() {
            private boolean state = initialState;
            @Override
            public void mouseClicked(MouseEvent e) {
                if(state){
                    firstState.onToggle();
                    state = false;
                }else {
                    secondState.onToggle();
                    state = true;
                }
            }
        });
        return button;
    }

    /**
     * Get button with icon
     * @param iconPath icon path from maven resources
     * @param iconSize icon size
     * @return JButton object with icon
     */
    public JButton getIconButton(String iconPath, float iconSize, Color background, String tooltip){
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
                button.setBackground(button.getBackground());
            }
        });
        if(tooltip.length() > 0) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    EventRouter.UI.fireEvent(new ShowTooltipEvent(tooltip, MouseInfo.getPointerInfo().getLocation()));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    EventRouter.UI.fireEvent(new HideTooltipEvent());
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    EventRouter.UI.fireEvent(new HideTooltipEvent());
                }
            });
        }
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    EventRouter.CORE.fireEvent(new ButtonPressedEvent());
                }
            }
        });
        button.setBorder(BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,4));
        button.setVerticalAlignment(SwingConstants.CENTER);
        BufferedImage icon = null;
        try {
            BufferedImage buttonIcon = ImageIO.read(getClass().getClassLoader().getResource(iconPath));
            icon = Scalr.resize(buttonIcon, (int)(scale*iconSize));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(icon != null){
            button.setIcon(new ImageIcon(icon));
        }
        return button;
    }

    public JButton getIconifiedTransparentButton(String iconPath, String tooltip){
        JButton iconButton = getIconButton(iconPath, 10, AppThemeColor.FRAME_RGB, tooltip);
        iconButton.setIcon(getImage(iconPath));
        return iconButton;
    }
    /**
     * Get bordered default button with icon
     * @param iconPath icon path from maven resources
     * @param iconSize icon size
     * @return bordered JButton with icon
     */
    public JButton getBorderedIconButton(String iconPath, int iconSize, String tooltip){
        CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1),
                BorderFactory.createLineBorder(AppThemeColor.BUTTON, 2)
        );
        JButton iconButton = getIconButton(iconPath, iconSize, AppThemeColor.FRAME_ALPHA,tooltip);
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
    public JButton getIconButton(String iconPath, int iconSize, Dimension buttonSize, String tooltip){
        JButton iconButton = getIconButton(iconPath, iconSize, AppThemeColor.FRAME_ALPHA, tooltip);
        iconButton.setPreferredSize(buttonSize); //todo scale
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
            label.setFont(getSelectedFont(fontStyle).deriveFont(scale*size));
        }else {
            label.setFont(DEFAULT_FONT.deriveFont(scale*size));
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

    public JLabel getTextLabel(FontStyle fontStyle, Color frColor, TextAlignment alignment, float size, Border border,String text){
        JLabel textLabel = getTextLabel(fontStyle, frColor, alignment, size, text);
        textLabel.setBorder(border);
        return textLabel;
    }

    /**
     * Get default label
     * @param text font text
     * @return JLabel object
     */
    public JLabel getTextLabel(String text){
        return getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT,TextAlignment.LEFTOP,scale*15f,text);
    }
    public JLabel getTextLabel(String text, FontStyle style){
        return getTextLabel(style,AppThemeColor.TEXT_DEFAULT,TextAlignment.LEFTOP,scale*15f,text);
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
            iconLabel.setIcon(getIcon(iconPath,(int)(scale*size)));
        } catch (Exception e) {
            return getTextLabel(StringUtils.substringBetween(iconPath,"/","."));
        }
        return iconLabel;
    }
    public JLabel getIconLabel(String iconPath, int size, String tooltip){
        JLabel iconLabel = new JLabel();
        try {
            iconLabel.setIcon(getIcon(iconPath,(int)(scale*size)));
        } catch (Exception e) {
            return getTextLabel(StringUtils.substringBetween(iconPath,"/","."));
        }
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                EventRouter.UI.fireEvent(new ShowTooltipEvent(tooltip, MouseInfo.getPointerInfo().getLocation()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                EventRouter.UI.fireEvent(new HideTooltipEvent());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.UI.fireEvent(new HideTooltipEvent());
            }
        });
        return iconLabel;
    }
    public JLabel getIconLabel(String iconPath){
        JLabel iconLabel = new JLabel();
        try {
            BufferedImage buttonIcon = ImageIO.read(getClass().getClassLoader().getResource(iconPath));
            iconLabel.setIcon(new ImageIcon(buttonIcon));
        } catch (Exception e) {
            return getTextLabel(StringUtils.substringBetween(iconPath,"/","."));
        }
        return iconLabel;
    }

    public JTextField getTextField(String text){
        JTextField textField = getTextField(text,null,scale*16);
        textField.setFont(DEFAULT_FONT);
        return textField;
    }
    public JTextField getTextField(String text, FontStyle style, float fontSize){
        JTextField textField = new JTextField(text);
        if(style != null) {
            textField.setFont(getSelectedFont(style).deriveFont(scale*fontSize));
        }
        textField.setForeground(AppThemeColor.TEXT_DEFAULT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TEXT_DEFAULT,1),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,3)
        ));
        textField.setBackground(AppThemeColor.HEADER);
        return textField;
    }

    public JCheckBox getCheckBox(String tooltip){
        JCheckBox checkBox = new JCheckBox();
        checkBox.setBackground(AppThemeColor.TRANSPARENT);
        checkBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                EventRouter.UI.fireEvent(new ShowTooltipEvent(tooltip, MouseInfo.getPointerInfo().getLocation()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                EventRouter.UI.fireEvent(new HideTooltipEvent());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.UI.fireEvent(new HideTooltipEvent());
            }
        });
        return checkBox;
    }
    public Font getFontByLang(String text,FontStyle style){
        if(style != null) {
            if(isAscii(text)){
                return getSelectedFont(style);
            }
        }
        return DEFAULT_FONT;
    }
    public JPanel getSliderSettingsPanel(JLabel titleLabel, JLabel countLabel, JSlider slider){
        Dimension elementsSize = convertSize(new Dimension(160, 30));
        Dimension countSize = convertSize(new Dimension(40, 30));
        titleLabel.setPreferredSize(elementsSize);
        slider.setPreferredSize(elementsSize);
        countLabel.setPreferredSize(countSize);
        JPanel panel = getTransparentPanel(new GridBagLayout());
        GridBagConstraints titleGc = new GridBagConstraints();
        GridBagConstraints countGc = new GridBagConstraints();
        GridBagConstraints sliderGc = new GridBagConstraints();
        titleGc.weightx = 0.5f;
        countGc.weightx = 0.1f;
        sliderGc.weightx = 0.4f;
        titleGc.fill = GridBagConstraints.HORIZONTAL;
        countGc.fill = GridBagConstraints.HORIZONTAL;
        sliderGc.fill = GridBagConstraints.HORIZONTAL;
        titleGc.anchor = GridBagConstraints.NORTHWEST;
        countGc.anchor = GridBagConstraints.NORTHWEST;
        sliderGc.anchor = GridBagConstraints.NORTHWEST;
        titleGc.gridx = 0;
        countGc.gridx = 1;
        sliderGc.gridx = 2;

        panel.add(titleLabel,titleGc);
        panel.add(countLabel,countGc);
        panel.add(slider,sliderGc);
        return panel;
    }
    public JPanel getSettingsPanel(JLabel titleLabel, Component component){
        Dimension elementsSize = convertSize(new Dimension(130, 30));
        titleLabel.setPreferredSize(elementsSize);
        component.setPreferredSize(elementsSize);
        JPanel panel = getTransparentPanel(new GridBagLayout());
        GridBagConstraints titleGc = new GridBagConstraints();
        GridBagConstraints sliderGc = new GridBagConstraints();
        titleGc.weightx = 0.5f;
        sliderGc.weightx = 0.5f;
        titleGc.fill = GridBagConstraints.HORIZONTAL;
        sliderGc.fill = GridBagConstraints.HORIZONTAL;
        titleGc.anchor = GridBagConstraints.NORTHWEST;
        sliderGc.anchor = GridBagConstraints.NORTHWEST;
        titleGc.gridx = 0;
        sliderGc.gridx = 1;

        panel.add(titleLabel,titleGc);
        panel.add(component,sliderGc);
        return panel;
    }
    public Font getFont(FontStyle style, float fontSize){
        return getSelectedFont(style).deriveFont(scale * fontSize);
    }
    public JComboBox getComboBox(String[] childs){
        JComboBox comboBox = new JComboBox(childs);
        comboBox.setBackground(AppThemeColor.HEADER);
        comboBox.setForeground(AppThemeColor.TEXT_DEFAULT);
        comboBox.setFont(BOLD_FONT.deriveFont(scale*16f));
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
    public JPanel getTransparentPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(AppThemeColor.TRANSPARENT);
        return panel;
    }

    public JPanel getBorderedTransparentPanel(Border border, LayoutManager layout){
        JPanel panel = new JPanel(layout);
        panel.setBackground(AppThemeColor.TRANSPARENT);
        panel.setBorder(border);
        return panel;
    }
    public ImageIcon getIcon(String iconPath, float size){
        BufferedImage icon = null;
        try {
            BufferedImage buttonIcon = ImageIO.read(getClass().getClassLoader().getResource(iconPath));
            icon = Scalr.resize(buttonIcon, (int)(scale * size));
        } catch (IOException e) {
            log.error(e);
        }
        return new ImageIcon(icon);
    }
    public ImageIcon getImage(String iconPath){
        BufferedImage icon = null;
        try {
            icon = ImageIO.read(getClass().getClassLoader().getResource(iconPath));
        } catch (IOException e) {
            log.error(e);
        }
        return new ImageIcon(icon);
    }
    public Dimension convertSize(Dimension initialSize){
        return new Dimension((int)(initialSize.width * scale),(int)(initialSize.height*scale));
    }
    public JTextArea getSimpleTextAre(String text){
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setBackground(AppThemeColor.TRANSPARENT);
        area.setBorder(null);
        area.setFont(REGULAR_FONT.deriveFont(scale*16f));
        area.setForeground(AppThemeColor.TEXT_DEFAULT);
        return area;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
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
