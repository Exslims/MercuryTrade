package com.mercury.platform.ui.adr.components;


import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrDurationComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.components.panel.FieldValueListener;
import com.mercury.platform.ui.adr.validator.DoubleFieldValidator;
import com.mercury.platform.ui.adr.validator.FieldValidator;
import com.mercury.platform.ui.adr.validator.IntegerFieldValidator;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.dialog.DialogCallback;
import com.mercury.platform.ui.adr.dialog.AdrIconSelectDialog;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.*;

public class AdrComponentsFactory {
    private ComponentsFactory componentsFactory;
    private boolean allowed;
    private AdrIconSelectDialog adrIconSelectDialog;

    public AdrComponentsFactory(ComponentsFactory componentsFactory) {
        this.componentsFactory = componentsFactory;
        this.adrIconSelectDialog = new AdrIconSelectDialog(this.getIconBundle());
    }

    public JPanel getComponentSizePanel(AdrComponentDescriptor descriptor, boolean fromGroup){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 4,4,0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel widthLabel = this.componentsFactory.getTextLabel("Width:");
        JLabel heightLabel = this.componentsFactory.getTextLabel("Height:");
        JTextField widthField = this.getSmartField(descriptor.getSize().width, new IntegerFieldValidator(10,2000), value -> {
            descriptor.setSize(new Dimension(value, descriptor.getSize().height));
            if(descriptor instanceof AdrGroupDescriptor){
                ((AdrGroupDescriptor) descriptor).getCells().forEach(item -> item.setSize(descriptor.getSize()));
            }
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField heightField = this.getSmartField(descriptor.getSize().height, new IntegerFieldValidator(10,1000), value -> {
            descriptor.setSize(new Dimension(descriptor.getSize().width,value));
            if(descriptor instanceof AdrGroupDescriptor){
                ((AdrGroupDescriptor) descriptor).getCells().forEach(item -> item.setSize(descriptor.getSize()));
            }
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
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
        JTextField xField = this.getSmartField(descriptor.getLocation().x,new IntegerFieldValidator(0,10000),value -> {
            descriptor.setLocation(new Point(value, descriptor.getLocation().y));
            if(descriptor instanceof AdrGroupDescriptor){
                ((AdrGroupDescriptor) descriptor).getCells().forEach(item -> item.setLocation(descriptor.getLocation()));
            }
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField yField = this.getSmartField(descriptor.getLocation().y,new IntegerFieldValidator(0,5000),value -> {
            descriptor.setLocation(new Point(descriptor.getLocation().x,value));
            if(descriptor instanceof AdrGroupDescriptor){
                ((AdrGroupDescriptor) descriptor).getCells().forEach(item -> item.setLocation(descriptor.getLocation()));
            }
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        root.add(xLabel);
        root.add(xField);
        root.add(yLabel);
        root.add(yField);

        MercuryStoreUI.adrUpdateSubject.subscribe(source -> {
            if(source.equals(descriptor)){
                xField.setText(String.valueOf(descriptor.getLocation().x));
                yField.setText(String.valueOf(descriptor.getLocation().y));
                if(descriptor instanceof AdrGroupDescriptor){
                    ((AdrGroupDescriptor) descriptor).getCells().forEach(item -> item.setLocation(descriptor.getLocation()));
                }
            }
        });

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
        JLabel iconLabel = this.componentsFactory.getIconLabel("app/adr/icons/" + descriptor.getIconPath() + ".png", 26);
        JLabel iconPathLabel = this.componentsFactory.getTextLabel(descriptor.getIconPath());
        root.add(iconLabel, BorderLayout.LINE_START);
        root.add(iconPathLabel,BorderLayout.CENTER);
        JButton selectIcon = this.componentsFactory.getBorderedButton("Select");
        selectIcon.addActionListener(action -> {
            this.adrIconSelectDialog.setSelectedIcon(descriptor.getIconPath());
            this.adrIconSelectDialog.setCallback(selectedIconPath -> {
                descriptor.setIconPath(selectedIconPath);
                iconLabel.setIcon(this.componentsFactory.getIcon("app/adr/icons/" + descriptor.getIconPath() + ".png",26));
                iconPathLabel.setText(descriptor.getIconPath());

                MercuryStoreUI.adrReloadSubject.onNext(descriptor);
            });
            this.adrIconSelectDialog.setLocationRelativeTo(root);
            this.adrIconSelectDialog.setVisible(true);
        });
        root.add(selectIcon,BorderLayout.LINE_END);
        return root;
    }
    public JPanel getTextColorPanel(AdrDurationComponentDescriptor descriptor){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1,7,2,0));
        root.setBackground(AppThemeColor.SLIDE_BG);

        JColorChooser colorChooser = getColorChooser();

        JPanel defaultValuePanel = new JPanel();
        JPanel mediumValuePanel = new JPanel();
        JPanel lowValuePanel = new JPanel();

        defaultValuePanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));
        mediumValuePanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));
        lowValuePanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));

        defaultValuePanel.setBackground(descriptor.getDefaultValueTextColor());
        mediumValuePanel.setBackground(descriptor.getMediumValueTextColor());
        lowValuePanel.setBackground(descriptor.getLowValueTextColor());

        JLabel conditionLabel = this.componentsFactory.getTextLabel("<",FontStyle.BOLD,24);
        JLabel conditionLabel1 = this.componentsFactory.getTextLabel("<",FontStyle.BOLD,24);
        conditionLabel.setBorder(null);
        conditionLabel1.setBorder(null);
        conditionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        conditionLabel1.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField mediumThreshold = this.getSmartField(
                descriptor.getMediumValueTextThreshold(),
                new DoubleFieldValidator(0.1, 1000.0),
                descriptor::setMediumValueTextThreshold);
        JTextField defaultThreshold = this.getSmartField(
                descriptor.getDefaultValueTextThreshold(),
                new DoubleFieldValidator(0.1, 1000.0),
                descriptor::setDefaultValueTextThreshold);

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
        root.add(lowValuePanel);
        root.add(conditionLabel);
        root.add(mediumThreshold);
        root.add(mediumValuePanel);
        root.add(conditionLabel1);
        root.add(defaultThreshold);
        root.add(defaultValuePanel);
        return root;
    }

    public JPanel getColorPickerPanel(Color initColor, DialogCallback<Color> callback){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1,1,6,0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JColorChooser colorChooser = getColorChooser();
        JPanel colorPanel = new JPanel();
        colorPanel.addMouseListener(new ColorChooserMouseListener(colorPanel){
            @Override
            public void mouseClicked(MouseEvent e) {
                colorChooser.setColor(initColor);
                JDialog dialog = JColorChooser.createDialog(colorPanel,
                        "Set color",
                        true,
                        colorChooser,
                        action -> {
                    callback.onAction(colorChooser.getColor());
                    colorPanel.setBackground(colorChooser.getColor());
                    },
                        null);
                dialog.setVisible(true);
            }
        });

        colorPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));
        colorPanel.setBackground(initColor);

        root.add(colorPanel);

        JPanel wrapper = this.componentsFactory.getJPanel(new BorderLayout());
        wrapper.setBackground(AppThemeColor.SLIDE_BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(6,0,6,0));
        wrapper.add(root,BorderLayout.CENTER);
        return wrapper;
    }

    public <T> JTextField getSmartField(T value, FieldValidator<String,T> validator, FieldValueListener<T> listener){
        JTextField field = this.componentsFactory.getTextField(String.valueOf(value),FontStyle.REGULAR,16f);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(validator.validate(field.getText())){
                    listener.onAction(validator.getValue());
                }else {
                    field.setText(String.valueOf(value));
                }
            }
        });
        return field;
    }

    public JPanel getBorderColorPanel(AdrDurationComponentDescriptor descriptor, DialogCallback<Color> callback){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(AppThemeColor.SLIDE_BG);
        JCheckBox checkBox = this.componentsFactory.getCheckBox(descriptor.isBindToTextColor(),"Bind to text color?");
        checkBox.addActionListener(e -> descriptor.setBindToTextColor(checkBox.isSelected()));
        JPanel colorPickerPanel = this.getColorPickerPanel(descriptor.getBorderColor(), value -> {
            checkBox.setSelected(false);
            callback.onAction(value);
        });

        root.add(checkBox,BorderLayout.LINE_START);
        root.add(colorPickerPanel,BorderLayout.CENTER);
        return root;
    }

    public JPanel getGapPanel(AdrGroupDescriptor descriptor){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 4,4,0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel hGap = this.componentsFactory.getTextLabel("hGap:");
        JLabel vGap = this.componentsFactory.getTextLabel("vGap:");
        JTextField hGapField = this.getSmartField(descriptor.getHGap(),new IntegerFieldValidator(0,200),value -> {
            descriptor.setHGap(value);
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField vGapField = this.getSmartField(descriptor.getVGap(),new IntegerFieldValidator(0,200),value -> {
            descriptor.setVGap(value);
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });

        root.add(hGap);
        root.add(hGapField);
        root.add(vGap);
        root.add(vGapField);
        return root;
    }
    public JPopupMenu getContextMenu(AdrComponentDescriptor selectedDescriptor) {
        JPopupMenu contextMenu = this.componentsFactory.getContextPanel();
        switch (selectedDescriptor.getType()){
            case GROUP: {
                JMenuItem addComponent = this.componentsFactory.getMenuItem("Add");
                JMenuItem iconComponent = this.componentsFactory.getMenuItem("Icon");
                JMenuItem pbComponent = this.componentsFactory.getMenuItem("Progress bar");
                addComponent.add(iconComponent);
                addComponent.add(pbComponent);
                contextMenu.add(addComponent);
                break;
            }
        }
        JMenuItem duplicateComponent = this.componentsFactory.getMenuItem("Duplicate");
        JMenuItem removeComponent = this.componentsFactory.getMenuItem("Remove");
        contextMenu.add(duplicateComponent);
        contextMenu.add(removeComponent);
        return contextMenu;
    }

    public JPanel getRightComponentOperationsPanel(AdrComponentDescriptor descriptor) {
        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 15, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_REMOVE_BUTTON);
        JButton visibleButton = this.componentsFactory.getIconButton("app/adr/visible_node_on.png", 15, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_EXPORT_BUTTON);
        visibleButton.addActionListener(action -> {
            if(descriptor.isVisible()){
                visibleButton.setIcon(this.componentsFactory.getIcon("app/adr/visible_node_off.png",15));
                descriptor.setVisible(false);
            }else {
                visibleButton.setIcon(this.componentsFactory.getIcon("app/adr/visible_node_on.png",15));
                descriptor.setVisible(true);
            }
        });
        JPanel buttonsPanel = this.componentsFactory.getJPanel(new GridLayout(2, 1));
        buttonsPanel.setBackground(AppThemeColor.SLIDE_BG);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(visibleButton);
        return buttonsPanel;
    }
    public JPanel getLeftComponentOperationsPanel(AdrComponentDescriptor descriptor) {
        JButton duplicateButton = this.componentsFactory.getIconButton("app/adr/duplicate_node.png", 16, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_EXPORT_BUTTON);
        JButton moveButton = this.componentsFactory.getIconButton("app/adr/move_node.png", 15, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_EXPORT_BUTTON);
        moveButton.addActionListener(action -> {
            JPopupMenu contextMenu = this.getContextMenu(descriptor);
            contextMenu.show(moveButton,8,8);
        });
        JPanel buttonsPanel = this.componentsFactory.getJPanel(new GridLayout(2, 2));
        buttonsPanel.setBackground(AppThemeColor.SLIDE_BG);
        buttonsPanel.add(moveButton);
        buttonsPanel.add(duplicateButton);
        return buttonsPanel;
    }

    public JPanel getCounterPanel(JPanel innerPanel, String title, Color bg, boolean initial){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(bg);

        JPanel headerPanel = this.componentsFactory.getJPanel(new BorderLayout());
        headerPanel.setBackground(bg);
        JLabel titleLabel = this.componentsFactory.getTextLabel(title);
        JButton counterButton = this.getCounterButton(innerPanel,bg,initial);
        headerPanel.add(titleLabel,BorderLayout.CENTER);
        headerPanel.add(counterButton,BorderLayout.LINE_START);
        root.add(headerPanel,BorderLayout.PAGE_START);
        root.add(innerPanel,BorderLayout.CENTER);
        return root;
    }

    public JButton getCounterButton(JPanel targetPanel,Color bg, boolean initial){
        String initialIcon = (initial)?"app/adr/collapse_icon.png" : "app/adr/expand_icon.png";
        JButton expandButton = this.componentsFactory.getIconButton(initialIcon, 16, AppThemeColor.FRAME, "");
        expandButton.setBackground(bg);
        expandButton.addActionListener(action -> {
            if(targetPanel.isVisible()){
                expandButton.setIcon(this.componentsFactory.getIcon("app/adr/expand_icon.png",16));
                targetPanel.setVisible(false);
            }else {
                expandButton.setIcon(this.componentsFactory.getIcon("app/adr/collapse_icon.png",16));
                targetPanel.setVisible(true);
            }
            MercuryStoreUI.adrUpdateTree.onNext(true); //todo
        });
        return expandButton;
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

    private String[] getIconBundle() {
        return new String[] {
                "Arctic_Armour_skill_icon",
                "Bismuth_Flask",
                "Bleeding_Immunity",
                "Blood_Rage_skill_icon",
                "Chill_And_Freeze_Immunity",
                "default_icon",
                "Diamond_Flask",
                "Granite_Flask",
                "Increase_Movement_Speed",
                "Jade_Flask",
                "Phase_Run_skill_icon",
                "Quicksilver_Flask",
                "Ruby_Flask",
                "Silver_Flask",
                "Stibnite_Flask",
                "Topaz_Flask",
                "Witchfire_Brew"
        };
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
