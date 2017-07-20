package com.mercury.platform.ui.adr.components;


import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrDurationComponentDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.plaf.synth.SynthColorChooserUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdrComponentsFactory {
    private ComponentsFactory componentsFactory;
    private boolean allowed;

    public AdrComponentsFactory(ComponentsFactory componentsFactory) {
        this.componentsFactory = componentsFactory;
    }

    public JPanel getIconSizePanel(AdrComponentDescriptor descriptor, boolean fromGroup){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 4,4,0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel widthLabel = this.componentsFactory.getTextLabel("Width:");
        JLabel heightLabel = this.componentsFactory.getTextLabel("Height:");
        JFormattedTextField widthField = this.componentsFactory.getIntegerTextField(10,1000,descriptor.getSize().width);
        JFormattedTextField heightField = this.componentsFactory.getIntegerTextField(10,1000,descriptor.getSize().height);

        widthField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    descriptor.setSize(new Dimension(Integer.parseInt(widthField.getText()), descriptor.getSize().height));
                }catch (NumberFormatException e1){
                    widthField.setValue(descriptor.getSize().width);
                }
            }
        });
        heightLabel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    descriptor.setSize(new Dimension(descriptor.getSize().width,Integer.parseInt(heightField.getText())));
                }catch (NumberFormatException e1){
                    heightField.setValue(descriptor.getSize().height);
                }
            }
        });
        root.add(widthLabel);
        root.add(widthField);
        root.add(heightLabel);
        root.add(heightField);

        if(fromGroup){
            widthField.setEnabled(false);
            heightField.setEnabled(false);
        }
        return root;
    }

    public JPanel getLocationPanel(AdrComponentDescriptor descriptor, boolean fromGroup){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 4,4,0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel xLabel = this.componentsFactory.getTextLabel("X:");
        JLabel yLabel = this.componentsFactory.getTextLabel("Y:");
        JFormattedTextField xField = this.componentsFactory.getIntegerTextField(1,3000,descriptor.getLocation().x);
        JFormattedTextField yField = this.componentsFactory.getIntegerTextField(1,3000,descriptor.getLocation().y);

        xField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    descriptor.setLocation(new Point(Integer.parseInt(xField.getText()), descriptor.getLocation().y));
                }catch (NumberFormatException e1){
                    xField.setValue(descriptor.getLocation().x);
                }
            }
        });
        yLabel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    descriptor.setLocation(new Point(descriptor.getLocation().x,Integer.parseInt(yField.getText())));
                }catch (NumberFormatException e1){
                    yField.setValue(descriptor.getLocation().y);
                }
            }
        });
        root.add(xLabel);
        root.add(xField);
        root.add(yLabel);
        root.add(yField);

        if(fromGroup){
            xField.setEnabled(false);
            yField.setEnabled(false);
        }
        return root;
    }

    public JButton getHotKeyButton(HotKeyDescriptor descriptor){
        JButton button = this.componentsFactory.getBorderedButton(this.getButtonText(descriptor));
        button.setFont(this.componentsFactory.getFont(FontStyle.BOLD,18f));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                button.setBackground(AppThemeColor.SLIDE_BG);
                button.setText("Press any key");
                allowed = true;
            }
        });
        MercuryStoreCore.hotKeySubject.subscribe(hotKey -> {
            if(allowed) {
                button.setBackground(AppThemeColor.BUTTON);

                descriptor.setVirtualKeyCode(hotKey.getVirtualKeyCode());
                descriptor.setKeyChar(hotKey.getKeyChar());
                descriptor.setShiftPressed(hotKey.isShiftPressed());
                descriptor.setMenuPressed(hotKey.isMenuPressed());
                descriptor.setExtendedKey(hotKey.isExtendedKey());
                descriptor.setControlPressed(hotKey.isControlPressed());

                button.setText(getButtonText(hotKey));
                allowed = false;
            }
        });
        return button;
    }

    public JPanel getIconSelectPanel(AdrDurationComponentDescriptor descriptor){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(AppThemeColor.SLIDE_BG);
        root.add(this.componentsFactory.getIconLabel("app/adr/"+descriptor.getIconPath()+".png",24),BorderLayout.LINE_START);
        root.add(this.componentsFactory.getTextLabel(descriptor.getIconPath()),BorderLayout.CENTER);
        JButton selectIcon = this.componentsFactory.getBorderedButton("Select");
        root.add(selectIcon,BorderLayout.LINE_END);
        return root;
    }
    public JPanel getTextColorPanel(AdrDurationComponentDescriptor descriptor){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1,3,6,0));
        root.setBackground(AppThemeColor.SLIDE_BG);

        JColorChooser colorChooser = getColorChooser();

        JPanel defaultValuePanel = new JPanel();
        JPanel mediumValuePanel = new JPanel();
        JPanel lowValuePanel = new JPanel();

        defaultValuePanel.addMouseListener(new ColorChooserMouseListener(defaultValuePanel){
            @Override
            public void mouseClicked(MouseEvent e) {
                colorChooser.setColor(descriptor.getDefaultValueTextColor());
                JDialog dialog = JColorChooser.createDialog(defaultValuePanel,
                        "Set default text color",
                        true,
                        colorChooser,
                        action -> {
                            descriptor.setDefaultValueTextColor(colorChooser.getColor());
                            defaultValuePanel.setBackground(colorChooser.getColor());
                        },
                        null);
                dialog.setVisible(true);
            }
        });
        mediumValuePanel.addMouseListener(new ColorChooserMouseListener(mediumValuePanel){
            @Override
            public void mouseClicked(MouseEvent e) {
                colorChooser.setColor(descriptor.getMediumValueTextColor());
                JDialog dialog = JColorChooser.createDialog(mediumValuePanel,
                        "Set text color",
                        true,
                        colorChooser,
                        action -> {
                            descriptor.setMediumValueTextColor(colorChooser.getColor());
                            mediumValuePanel.setBackground(colorChooser.getColor());
                        },
                        null);
                dialog.setVisible(true);
            }
        });
        lowValuePanel.addMouseListener(new ColorChooserMouseListener(lowValuePanel){
            @Override
            public void mouseClicked(MouseEvent e) {
                colorChooser.setColor(descriptor.getLowValueTextColor());
                JDialog dialog = JColorChooser.createDialog(lowValuePanel,
                        "Set text color",
                        true,
                        colorChooser,
                        action -> {
                            descriptor.setLowValueTextColor(colorChooser.getColor());
                            lowValuePanel.setBackground(colorChooser.getColor());
                        },
                        null);
                dialog.setVisible(true);
            }
        });

        defaultValuePanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));
        mediumValuePanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));
        lowValuePanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));

        defaultValuePanel.setBackground(descriptor.getDefaultValueTextColor());
        mediumValuePanel.setBackground(descriptor.getMediumValueTextColor());
        lowValuePanel.setBackground(descriptor.getLowValueTextColor());

        root.add(defaultValuePanel);
        root.add(mediumValuePanel);
        root.add(lowValuePanel);



        JPanel wrapper = this.componentsFactory.getJPanel(new BorderLayout());
        wrapper.setBackground(AppThemeColor.SLIDE_BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(6,0,6,0));
        wrapper.add(root,BorderLayout.CENTER);
        return wrapper;
    }

    private JColorChooser getColorChooser(){
        JColorChooser colorChooser = new JColorChooser();
        String type = UIManager.getString("ColorChooser.hsvNameText", colorChooser.getLocale());
        for (AbstractColorChooserPanel p: colorChooser.getChooserPanels()) {
            if (!p.getDisplayName().equals(type)) {
                colorChooser.removeChooserPanel(p);
            }
        }
        return colorChooser;
    }
    private String getButtonText(HotKeyDescriptor descriptor){
        if(descriptor.getKeyChar() == '\u0000') {
            return "...";
        }
        String text = String.valueOf(descriptor.getKeyChar());
        if(descriptor.isShiftPressed())
            text = "Shift + " + text;
        if(descriptor.isMenuPressed())
            text = "Alt + " + text;
        if(descriptor.isControlPressed())
            text = "Ctrl + " + text;
        return text;
    }

    private class ColorChooserMouseListener extends MouseAdapter {
        private JPanel panel;

        public ColorChooserMouseListener(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            this.panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            this.panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
