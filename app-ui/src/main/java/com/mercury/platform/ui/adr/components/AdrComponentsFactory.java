package com.mercury.platform.ui.adr.components;


import com.mercury.platform.shared.CloneHelper;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.IconBundleConfigurationService;
import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.components.panel.FieldValueListener;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;
import com.mercury.platform.ui.adr.components.panel.ui.ValueBinder;
import com.mercury.platform.ui.adr.dialog.ExportHelper;
import com.mercury.platform.ui.adr.routing.AdrComponentDefinition;
import com.mercury.platform.ui.adr.routing.AdrComponentOperations;
import com.mercury.platform.ui.adr.validator.DoubleFieldValidator;
import com.mercury.platform.ui.adr.validator.FieldValidator;
import com.mercury.platform.ui.adr.validator.HexFieldValidator;
import com.mercury.platform.ui.adr.validator.IntegerFieldValidator;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.dialog.AlertDialog;
import com.mercury.platform.ui.dialog.DialogCallback;
import com.mercury.platform.ui.adr.dialog.AdrIconSelectDialog;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class AdrComponentsFactory {
    private ComponentsFactory componentsFactory;
    private boolean allowed;
    private IconBundleConfigurationService config;

    public AdrComponentsFactory(ComponentsFactory componentsFactory) {
        this.componentsFactory = componentsFactory;
        this.config = Configuration.get().iconBundleConfiguration();
    }

    public JPanel getComponentSizePanel(AdrComponentDescriptor descriptor, boolean fromGroup){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 4,4,0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel widthLabel = this.componentsFactory.getTextLabel("Width:");
        JLabel heightLabel = this.componentsFactory.getTextLabel("Height:");
        JTextField widthField = this.getSmartField(descriptor.getSize().width, new IntegerFieldValidator(10,2000), value -> {
            descriptor.setSize(new Dimension(value, descriptor.getSize().height));
            if(descriptor instanceof AdrTrackerGroupDescriptor){
                ((AdrTrackerGroupDescriptor) descriptor).getCells().forEach(item -> item.setSize(descriptor.getSize()));
            }
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField heightField = this.getSmartField(descriptor.getSize().height, new IntegerFieldValidator(10,1000), value -> {
            descriptor.setSize(new Dimension(descriptor.getSize().width,value));
            if(descriptor instanceof AdrTrackerGroupDescriptor){
                ((AdrTrackerGroupDescriptor) descriptor).getCells().forEach(item -> item.setSize(descriptor.getSize()));
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
            if(descriptor instanceof AdrTrackerGroupDescriptor){
                ((AdrTrackerGroupDescriptor) descriptor).getCells().forEach(item -> item.setLocation(descriptor.getLocation()));
            }
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField yField = this.getSmartField(descriptor.getLocation().y,new IntegerFieldValidator(0,5000),value -> {
            descriptor.setLocation(new Point(descriptor.getLocation().x,value));
            if(descriptor instanceof AdrTrackerGroupDescriptor){
                ((AdrTrackerGroupDescriptor) descriptor).getCells().forEach(item -> item.setLocation(descriptor.getLocation()));
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
                if(descriptor instanceof AdrTrackerGroupDescriptor){
                    ((AdrTrackerGroupDescriptor) descriptor).getCells().forEach(item -> item.setLocation(descriptor.getLocation()));
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
        JLabel iconLabel = this.componentsFactory.getIconLabel(this.config.getIcon(descriptor.getIconPath()), 26);
        JLabel iconPathLabel = this.componentsFactory.getTextLabel(descriptor.getIconPath());
        root.add(iconLabel, BorderLayout.LINE_START);
        root.add(iconPathLabel,BorderLayout.CENTER);
        JButton selectIcon = this.componentsFactory.getBorderedButton("Select");
        selectIcon.addActionListener(action -> {
            AdrIconSelectDialog adrIconSelectDialog = new AdrIconSelectDialog();
            adrIconSelectDialog.setSelectedIcon(descriptor.getIconPath());
            adrIconSelectDialog.setCallback(selectedIconPath -> {
                descriptor.setIconPath(selectedIconPath);
                iconLabel.setIcon(this.componentsFactory.getIcon(this.config.getIcon(selectedIconPath),26));
                iconPathLabel.setText(descriptor.getIconPath());

                MercuryStoreUI.adrReloadSubject.onNext(descriptor);
            });
            adrIconSelectDialog.setLocationRelativeTo(root);
            adrIconSelectDialog.setVisible(true);
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

    public JPanel getColorPickerPanel(ValueBinder<Color> binder, DialogCallback<Color> callback){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1,1,6,0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JColorChooser colorChooser = getColorChooser();
        JPanel colorPanel = new JPanel();
        colorPanel.addMouseListener(new ColorChooserMouseListener(colorPanel){
            @Override
            public void mouseClicked(MouseEvent e) {
                colorChooser.setColor(binder.getValue());
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
        colorPanel.setBackground(binder.getValue());

        root.add(colorPanel);

        JPanel wrapper = this.componentsFactory.getJPanel(new BorderLayout());
        wrapper.setBackground(AppThemeColor.SLIDE_BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(6,0,6,0));
        wrapper.add(root,BorderLayout.CENTER);
        return wrapper;
    }
    public JPanel getHexColorPickerPanel(ValueBinder<Color> colorBinder, DialogCallback<Color> callback){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(6,0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JColorChooser colorChooser = getColorChooser();
        JPanel colorPanel = new JPanel();
        JTextField hexField = this.getSmartField(
                "#" + Integer.toHexString(colorBinder.getValue().getRGB() & 0xffffff),
                new HexFieldValidator(),
                value -> {
                    Color decoded = Color.decode(value);
                    callback.onAction(decoded);
                    colorPanel.setBackground(decoded);
                });
        hexField.setPreferredSize(new Dimension(76,26));
        colorPanel.addMouseListener(new ColorChooserMouseListener(colorPanel){
            @Override
            public void mouseClicked(MouseEvent e) {
                colorChooser.setColor(colorBinder.getValue());
                JDialog dialog = JColorChooser.createDialog(colorPanel,
                        "Set color",
                        true,
                        colorChooser,
                        action -> {
                            callback.onAction(colorChooser.getColor());
                            colorPanel.setBackground(colorChooser.getColor());
                            hexField.setText("#" + Integer.toHexString(colorChooser.getColor().getRGB() & 0xffffff));
                        },
                        null);
                dialog.setVisible(true);
            }
        });

        colorPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));
        colorPanel.setBackground(colorBinder.getValue());

        root.add(hexField,BorderLayout.LINE_START);
        root.add(colorPanel,BorderLayout.CENTER);

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
        JPanel colorPickerPanel = this.getColorPickerPanel(descriptor::getBorderColor, value -> {
            checkBox.setSelected(false);
            callback.onAction(value);
        });

        root.add(checkBox,BorderLayout.LINE_START);
        root.add(colorPickerPanel,BorderLayout.CENTER);
        return root;
    }

    public JPanel getGapPanel(AdrTrackerGroupDescriptor descriptor){
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
    public JPopupMenu getContextMenu(AdrTreeNode<AdrComponentDescriptor> treeNode) {
        JPopupMenu contextMenu = this.componentsFactory.getContextPanel();
        contextMenu.setBackground(AppThemeColor.ADR_BG);
        contextMenu.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        contextMenu.setForeground(AppThemeColor.TEXT_DEFAULT);

        if(treeNode.getParent().getData() != null){
            JMenuItem moveOut = this.componentsFactory.getMenuItem("Move out");
            moveOut.setBorder(BorderFactory.createMatteBorder(1,0,0,0,AppThemeColor.ADR_DEFAULT_BORDER));
            moveOut.addActionListener(action -> {
                MercuryStoreUI.adrRemoveComponentSubject.onNext(treeNode.getData());
                MercuryStoreUI.adrComponentStateSubject.onNext(
                        new AdrComponentDefinition(treeNode.getData(),
                                AdrComponentOperations.NEW_COMPONENT,
                                treeNode.getParent().getParent().getData())
                );
            });
            moveOut.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    contextMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    contextMenu.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
            contextMenu.add(moveOut);
        }
        treeNode.getParent().forEach(neighbor -> {
            if(neighbor.getData() instanceof AdrTrackerGroupDescriptor) {
                JMenuItem moveTo = this.componentsFactory.getMenuItem("Move to " + neighbor.getData().getTitle());
                moveTo.setBorder(BorderFactory.createMatteBorder(1,0,0,0,AppThemeColor.ADR_DEFAULT_BORDER));
                moveTo.addActionListener(action -> {
                    MercuryStoreUI.adrRemoveComponentSubject.onNext(treeNode.getData());
                    MercuryStoreUI.adrComponentStateSubject.onNext(
                            new AdrComponentDefinition(treeNode.getData(),
                                    AdrComponentOperations.NEW_COMPONENT,
                                    neighbor.getData())
                    );
                });
                moveTo.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        contextMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        contextMenu.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                });
                switch (((AdrTrackerGroupDescriptor)neighbor.getData()).getContentType()){
                    case ICONS:{
                        if(treeNode.getData() instanceof AdrIconDescriptor){
                            contextMenu.add(moveTo);
                        }
                        break;
                    }
                    case PROGRESS_BARS: {
                        if(treeNode.getData() instanceof AdrProgressBarDescriptor){
                            contextMenu.add(moveTo);
                        }
                        break;
                    }
                }
            }
        });
        JMenuItem export = this.componentsFactory.getMenuItem("Export");
        export.setBorder(BorderFactory.createMatteBorder(1,0,0,0,AppThemeColor.ADR_DEFAULT_BORDER));
        export.addActionListener(action -> {
            ExportHelper.exportComponent(treeNode.getData());
        });
        contextMenu.add(export);
        return contextMenu;
    }

    public JPanel getRightComponentOperationsPanel(AdrComponentDescriptor descriptor) {
        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 15, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_REMOVE_BUTTON);
        removeButton.addActionListener(action -> {
            new AlertDialog(this.getRemoveCallback(descriptor),"Do you want to delete \"" + descriptor.getTitle() + "\"component?",removeButton).setVisible(true);
        });
        JButton visibleButton = this.componentsFactory.getIconButton("app/adr/visible_node_on.png", 15, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_EXPORT_BUTTON);
        visibleButton.addActionListener(action -> {
            if(descriptor.isVisible()){
                visibleButton.setIcon(this.componentsFactory.getIcon("app/adr/visible_node_off.png",15));
                descriptor.setVisible(false);
            }else {
                visibleButton.setIcon(this.componentsFactory.getIcon("app/adr/visible_node_on.png",15));
                descriptor.setVisible(true);
            }
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JPanel buttonsPanel = this.componentsFactory.getJPanel(new GridLayout(2, 1));
        buttonsPanel.setBackground(AppThemeColor.SLIDE_BG);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(visibleButton);
        return buttonsPanel;
    }
    public JPanel getLeftComponentOperationsPanel(AdrTreeNode<AdrComponentDescriptor> treeNode) {
        JButton duplicateButton = this.componentsFactory.getIconButton("app/adr/duplicate_node.png", 16, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_EXPORT_BUTTON);
        duplicateButton.addActionListener(action -> {
            AdrComponentDescriptor cloned = CloneHelper.cloneObject(treeNode.getData());
            if(cloned != null) {
                cloned.setId(cloned.getId() + 1);
                cloned.setLocation(new Point(new Random().nextInt(500), new Random().nextInt(500)));
                MercuryStoreUI.adrComponentStateSubject.onNext(
                        new AdrComponentDefinition(cloned,
                                AdrComponentOperations.NEW_COMPONENT,
                                treeNode.getParent().getData()));
            }
        });
        JButton moveButton = this.componentsFactory.getIconButton("app/adr/move_node.png", 15, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_EXPORT_BUTTON);
        moveButton.addActionListener(action -> {
            JPopupMenu contextMenu = this.getContextMenu(treeNode);
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
            MercuryStoreUI.adrManagerPack.onNext(true); //todo
        });
        return expandButton;
    }
    public String getGroupTypeIconPath(AdrTrackerGroupDescriptor descriptor){
        String iconPath = "app/adr/static_group_icon.png";
        switch (descriptor.getGroupType()) {
            case STATIC: {
                iconPath = "app/adr/static_group_icon.png";
                break;
            }
            case DYNAMIC: {
                iconPath = "app/adr/dynamic_group_icon.png";
                break;
            }
        }
        return iconPath;
    }
    public DialogCallback<Boolean> getRemoveCallback(AdrComponentDescriptor descriptor){
        return success -> {
            if(success) {
                MercuryStoreUI.adrRemoveComponentSubject.onNext(descriptor);
                MercuryStoreUI.adrManagerPack.onNext(true);
            }
        };
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
