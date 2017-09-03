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
import com.mercury.platform.ui.adr.routing.AdrComponentDefinition;
import com.mercury.platform.ui.adr.routing.AdrComponentOperations;
import com.mercury.platform.ui.adr.validator.*;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.dialog.AlertDialog;
import com.mercury.platform.ui.dialog.DialogCallback;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.UUID;

public class AdrComponentsFactory {
    private ComponentsFactory componentsFactory;
    private boolean allowed;
    private IconBundleConfigurationService config;

    public AdrComponentsFactory(ComponentsFactory componentsFactory) {
        this.componentsFactory = componentsFactory;
        this.config = Configuration.get().iconBundleConfiguration();
    }

    public JPanel getComponentSizePanel(AdrComponentDescriptor descriptor, boolean fromGroup) {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 4, 4, 0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel widthLabel = this.componentsFactory.getTextLabel("Width:");
        JLabel heightLabel = this.componentsFactory.getTextLabel("Height:");
        JTextField widthField = this.getSmartField(descriptor.getSize().width, new IntegerFieldValidator(10, 2000), value -> {
            descriptor.setSize(new Dimension(value, descriptor.getSize().height));
            if (descriptor instanceof AdrTrackerGroupDescriptor) {
                ((AdrTrackerGroupDescriptor) descriptor).getCells().forEach(item -> item.setSize(descriptor.getSize()));
            }
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField heightField = this.getSmartField(descriptor.getSize().height, new IntegerFieldValidator(10, 1000), value -> {
            descriptor.setSize(new Dimension(descriptor.getSize().width, value));
            if (descriptor instanceof AdrTrackerGroupDescriptor) {
                ((AdrTrackerGroupDescriptor) descriptor).getCells().forEach(item -> item.setSize(descriptor.getSize()));
            }
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        MercuryStoreUI.adrUpdateSubject.subscribe(source -> {
            if (source.equals(descriptor)) {
                widthField.setText(String.valueOf(descriptor.getSize().width));
                heightField.setText(String.valueOf(descriptor.getSize().height));
                if (descriptor instanceof AdrTrackerGroupDescriptor) {
                    ((AdrTrackerGroupDescriptor) descriptor).getCells().forEach(item -> item.setSize(descriptor.getSize()));
                }
            }
        });

        root.add(widthLabel);
        root.add(widthField);
        root.add(heightLabel);
        root.add(heightField);

        if (fromGroup) {
            widthField.setEnabled(false);
            heightField.setEnabled(false);
        }
        return root;
    }

    public JPanel getSoundPanel(AdrDurationComponentDescriptor descriptor) {
        String[] soundPaths = {
                "...",
                "Button-chime",
                "Corsica-ding",
                "Foolboy-notification",
                "Nenadsimic"
        };
        JComboBox soundPathBox = this.componentsFactory.getComboBox(soundPaths);
        String selectedPath = "...";
        if (!descriptor.getSoundDescriptor().getWavPath().equals("...")) {
            selectedPath = StringUtils.substringBetween(descriptor.getSoundDescriptor().getWavPath(), "app/sounds/", ".wav");
        }
        soundPathBox.setSelectedItem(selectedPath);
        soundPathBox.addActionListener(action -> {
            if (!soundPathBox.getSelectedItem().equals("...")) {
                descriptor.getSoundDescriptor().setWavPath("app/sounds/" + soundPathBox.getSelectedItem() + ".wav");
                MercuryStoreCore.soundDescriptorSubject.onNext(descriptor.getSoundDescriptor());
                MercuryStoreUI.adrReloadSubject.onNext(descriptor);
            } else {
                descriptor.getSoundDescriptor().setWavPath("...");
            }
        });
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(4, 0), AppThemeColor.ADR_BG);
        JTextField durationField =
                this.getSmartField(descriptor.getSoundThreshold(),
                        new DoubleFieldValidator(0.0, 1000.0), descriptor::setSoundThreshold);
        durationField.setPreferredSize(new Dimension(36, 26));
        root.add(soundPathBox, BorderLayout.LINE_START);
        root.add(this.componentsFactory.getTextLabel("when duration ="), BorderLayout.CENTER);
        root.add(durationField, BorderLayout.LINE_END);
        return root;
    }

    public JSlider getVolumeSlider(AdrDurationComponentDescriptor descriptor) {
        JSlider notificationSlider = this.componentsFactory.getSlider(-40, 6, descriptor.getSoundDescriptor().getDb().intValue(), AppThemeColor.ADR_BG);
        notificationSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!descriptor.getSoundDescriptor().getWavPath().equals("...")) {
                    descriptor.getSoundDescriptor().setDb(notificationSlider.getValue() == -40 ? -80f : (float) notificationSlider.getValue());
                    MercuryStoreCore.soundDescriptorSubject.onNext(descriptor.getSoundDescriptor());
                    MercuryStoreUI.adrReloadSubject.onNext(descriptor);
                }
            }
        });
        return notificationSlider;
    }

    public JPanel getCaptureSizePanel(AdrCaptureDescriptor descriptor) {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 4, 4, 0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel widthLabel = this.componentsFactory.getTextLabel("Width:");
        JLabel heightLabel = this.componentsFactory.getTextLabel("Height:");
        JTextField widthField = this.getSmartField(descriptor.getCaptureSize().width, new IntegerFieldValidator(10, 2000), value -> {
            descriptor.setCaptureSize(new Dimension(value, descriptor.getCaptureSize().height));
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField heightField = this.getSmartField(descriptor.getCaptureSize().height, new IntegerFieldValidator(10, 1000), value -> {
            descriptor.setCaptureSize(new Dimension(descriptor.getCaptureSize().width, value));
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });

        root.add(widthLabel);
        root.add(widthField);
        root.add(heightLabel);
        root.add(heightField);
        return root;
    }

    public JPanel getLocationPanel(AdrComponentDescriptor descriptor, boolean fromGroup) {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 4, 4, 0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel xLabel = this.componentsFactory.getTextLabel("X:");
        JLabel yLabel = this.componentsFactory.getTextLabel("Y:");
        JTextField xField = this.getSmartField(descriptor.getLocation().x, new IntegerFieldValidator(0, 10000), value -> {
            descriptor.setLocation(new Point(value, descriptor.getLocation().y));
            if (descriptor instanceof AdrTrackerGroupDescriptor) {
                ((AdrTrackerGroupDescriptor) descriptor).getCells().forEach(item -> item.setLocation(descriptor.getLocation()));
            }
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField yField = this.getSmartField(descriptor.getLocation().y, new IntegerFieldValidator(0, 5000), value -> {
            descriptor.setLocation(new Point(descriptor.getLocation().x, value));
            if (descriptor instanceof AdrTrackerGroupDescriptor) {
                ((AdrTrackerGroupDescriptor) descriptor).getCells().forEach(item -> item.setLocation(descriptor.getLocation()));
            }
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        root.add(xLabel);
        root.add(xField);
        root.add(yLabel);
        root.add(yField);

        MercuryStoreUI.adrUpdateSubject.subscribe(source -> {
            if (source.equals(descriptor)) {
                xField.setText(String.valueOf(descriptor.getLocation().x));
                yField.setText(String.valueOf(descriptor.getLocation().y));
                if (descriptor instanceof AdrTrackerGroupDescriptor) {
                    ((AdrTrackerGroupDescriptor) descriptor).getCells().forEach(item -> item.setLocation(descriptor.getLocation()));
                }
            }
        });

        if (fromGroup) {
            xField.setEnabled(false);
            yField.setEnabled(false);
        }
        return root;
    }

    public JPanel getCaptureLocationPanel(AdrCaptureDescriptor descriptor) {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 4, 4, 0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel xLabel = this.componentsFactory.getTextLabel("X:");
        JLabel yLabel = this.componentsFactory.getTextLabel("Y:");
        JTextField xField = this.getSmartField(descriptor.getCaptureLocation().x, new IntegerFieldValidator(0, 10000), value -> {
            descriptor.setCaptureLocation(new Point(value, descriptor.getCaptureLocation().y));
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField yField = this.getSmartField(descriptor.getCaptureLocation().y, new IntegerFieldValidator(0, 5000), value -> {
            descriptor.setCaptureLocation(new Point(descriptor.getCaptureLocation().x, value));
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        root.add(xLabel);
        root.add(xField);
        root.add(yLabel);
        root.add(yField);

        MercuryStoreUI.adrUpdateSubject.subscribe(source -> {
            if (source.equals(descriptor)) {
                xField.setText(String.valueOf(descriptor.getCaptureLocation().x));
                yField.setText(String.valueOf(descriptor.getCaptureLocation().y));
            }
        });
        return root;
    }

    public JButton getHotKeyButton(AdrDurationComponentDescriptor descriptor) {
        JButton button = this.componentsFactory.getBorderedButton(descriptor.getHotKeyDescriptor().getTitle());
        button.setFont(this.componentsFactory.getFont(FontStyle.BOLD, 18f));
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    button.setBackground(AppThemeColor.SLIDE_BG);
                    button.setText("Press any key");
                    allowed = true;
                    button.removeMouseListener(this);
                }
            }
        };
        button.addMouseListener(mouseAdapter);
        MercuryStoreCore.hotKeySubject.subscribe(hotKey -> {
            if (allowed) {
                button.setBackground(AppThemeColor.BUTTON);
                if (hotKey.getVirtualKeyCode() == 1) {
                    descriptor.setHotKeyDescriptor(new HotKeyDescriptor());
                } else {
                    descriptor.setHotKeyDescriptor(hotKey);
                }
                button.setText(descriptor.getHotKeyDescriptor().getTitle());
                allowed = false;
                MercuryStoreUI.adrReloadSubject.onNext(descriptor);
                button.addMouseListener(mouseAdapter);
            }
        });
        return button;
    }

    public JPanel getHotKeyPanel(AdrDurationComponentDescriptor descriptor) {
        JButton hotKeyButton = this.getHotKeyButton(descriptor);
        JPanel hotKeyPanel = this.componentsFactory.getJPanel(new BorderLayout());
        hotKeyPanel.setBackground(AppThemeColor.SLIDE_BG);
        JCheckBox refreshBox = this.componentsFactory.getCheckBox(descriptor.isHotKeyRefresh(), "Always refresh?");
        refreshBox.addActionListener(state -> {
            descriptor.setHotKeyRefresh(refreshBox.isSelected());
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
            MercuryStoreUI.adrManagerPack.onNext(true);
        });
        hotKeyPanel.add(refreshBox, BorderLayout.LINE_START);
        hotKeyPanel.add(hotKeyButton, BorderLayout.CENTER);
        return hotKeyPanel;
    }

    public JPanel getInsetsPanel(AdrDurationComponentDescriptor descriptor) {
        JLabel topLabel = this.componentsFactory.getTextLabel("T:");
        JLabel leftLabel = this.componentsFactory.getTextLabel("L:");
        JLabel bottomLabel = this.componentsFactory.getTextLabel("B:");
        JLabel rightLabel = this.componentsFactory.getTextLabel("R:");

        JTextField topField = this.getSmartField(descriptor.getInsets().top, new IntegerFieldValidator(0, 300), value -> {
            Insets currentInsets = descriptor.getInsets();
            descriptor.setInsets(new Insets(value, currentInsets.left, currentInsets.bottom, currentInsets.right));
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField leftField = this.getSmartField(descriptor.getInsets().left, new IntegerFieldValidator(0, 300), value -> {
            Insets currentInsets = descriptor.getInsets();
            descriptor.setInsets(new Insets(currentInsets.top, value, currentInsets.bottom, currentInsets.right));
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField bottomField = this.getSmartField(descriptor.getInsets().bottom, new IntegerFieldValidator(0, 300), value -> {
            Insets currentInsets = descriptor.getInsets();
            descriptor.setInsets(new Insets(currentInsets.top, currentInsets.left, value, currentInsets.right));
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField rightField = this.getSmartField(descriptor.getInsets().right, new IntegerFieldValidator(0, 300), value -> {
            Insets currentInsets = descriptor.getInsets();
            descriptor.setInsets(new Insets(currentInsets.top, currentInsets.left, currentInsets.bottom, value));
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 8, 4, 0));
        root.setBackground(AppThemeColor.ADR_BG);
        root.add(topLabel);
        root.add(this.componentsFactory.wrapToSlide(topField, AppThemeColor.ADR_BG));
        root.add(leftLabel);
        root.add(this.componentsFactory.wrapToSlide(leftField, AppThemeColor.ADR_BG));
        root.add(bottomLabel);
        root.add(this.componentsFactory.wrapToSlide(bottomField, AppThemeColor.ADR_BG));
        root.add(rightLabel);
        root.add(this.componentsFactory.wrapToSlide(rightField, AppThemeColor.ADR_BG));
        return root;
    }

    public JPanel getIconSelectPanel(AdrDurationComponentDescriptor descriptor) {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(AppThemeColor.ADR_BG);
        JLabel iconLabel = this.componentsFactory.getIconLabel(this.config.getIcon(descriptor.getIconPath()), 26);
        JLabel iconPathLabel = this.componentsFactory.getTextLabel(descriptor.getIconPath());
        root.add(iconLabel, BorderLayout.LINE_START);
        root.add(iconPathLabel, BorderLayout.CENTER);
        JButton selectIcon = this.componentsFactory.getBorderedButton("Select");
        selectIcon.addActionListener(action -> {
            MercuryStoreUI.adrOpenIconSelectSubject.onNext(selectedIconPath -> {
                descriptor.setIconPath(selectedIconPath);
                iconLabel.setIcon(this.componentsFactory.getIcon(this.config.getIcon(selectedIconPath), 26));
                iconPathLabel.setText(descriptor.getIconPath());

                MercuryStoreUI.adrReloadSubject.onNext(descriptor);
            });
        });
        root.add(selectIcon, BorderLayout.LINE_END);
        return root;
    }

    private JPanel getTextColorPanel(AdrDurationComponentDescriptor descriptor) {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 7, 2, 0));
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

        JLabel conditionLabel = this.componentsFactory.getTextLabel("<", FontStyle.BOLD, 24);
        JLabel conditionLabel1 = this.componentsFactory.getTextLabel("<", FontStyle.BOLD, 24);
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

        defaultValuePanel.addMouseListener(new ColorChooserMouseListener(defaultValuePanel) {
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
        mediumValuePanel.addMouseListener(new ColorChooserMouseListener(mediumValuePanel) {
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
        lowValuePanel.addMouseListener(new ColorChooserMouseListener(lowValuePanel) {
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
        root.add(this.componentsFactory.wrapToSlide(lowValuePanel, AppThemeColor.ADR_BG, 4, 0, 4, 0));
        root.add(conditionLabel);
        root.add(this.componentsFactory.wrapToSlide(mediumThreshold, AppThemeColor.ADR_BG, 4, 0, 4, 0));
        root.add(this.componentsFactory.wrapToSlide(mediumValuePanel, AppThemeColor.ADR_BG, 4, 0, 4, 0));
        root.add(conditionLabel1);
        root.add(this.componentsFactory.wrapToSlide(defaultThreshold, AppThemeColor.ADR_BG, 4, 0, 4, 0));
        root.add(this.componentsFactory.wrapToSlide(defaultValuePanel, AppThemeColor.ADR_BG, 4, 0, 4, 0));
        return root;
    }

    public JComboBox getTextFormatBox(AdrDurationComponentDescriptor descriptor) {
        JComboBox textFormatBox = this.componentsFactory.getComboBox(new String[]{"0", "0.0", "0.00", "0.000"});
        textFormatBox.setSelectedItem(descriptor.getTextFormat());
        textFormatBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                descriptor.setTextFormat((String) textFormatBox.getSelectedItem());
                MercuryStoreUI.adrUpdateSubject.onNext(descriptor);
                MercuryStoreUI.adrReloadSubject.onNext(descriptor);
            }
        });
        return textFormatBox;
    }

    public JTextField getTitleField(AdrComponentDescriptor descriptor) {
        JTextField titleField = this.componentsFactory.getTextField(descriptor.getTitle(), FontStyle.REGULAR, 18);
        titleField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                descriptor.setTitle(titleField.getText());
                MercuryStoreUI.adrReloadSubject.onNext(descriptor);
            }
        });
        return titleField;
    }

    public JSlider getOpacitySlider(AdrComponentDescriptor descriptor) {
        JSlider opacitySlider = this.componentsFactory.getSlider(20, 100, (int) (descriptor.getOpacity() * 100));
        opacitySlider.setBackground(AppThemeColor.SLIDE_BG);
        opacitySlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                descriptor.setOpacity(opacitySlider.getValue() / 100f);
                MercuryStoreUI.adrReloadSubject.onNext(descriptor);
            }
        });
        return opacitySlider;
    }

    public JPanel getFpsSliderPanel(AdrCaptureDescriptor descriptor) {
        JPanel fpsPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        JLabel fpsCountLabel = this.componentsFactory.getTextLabel(String.valueOf(descriptor.getFps()));
        fpsCountLabel.setPreferredSize(new Dimension(30, 26));
        JSlider fpsSlider = this.componentsFactory.getSlider(1, 60, descriptor.getFps());
        fpsSlider.setBackground(AppThemeColor.SLIDE_BG);
        fpsSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                descriptor.setFps(fpsSlider.getValue());
                fpsCountLabel.setText(String.valueOf(fpsSlider.getValue()));
                MercuryStoreUI.adrReloadSubject.onNext(descriptor);
            }
        });
        fpsPanel.add(fpsCountLabel, BorderLayout.LINE_START);
        fpsPanel.add(fpsSlider, BorderLayout.CENTER);
        return fpsPanel;
    }

    public JTextField getFontSizeField(AdrDurationComponentDescriptor descriptor) {
        return this.getSmartField(descriptor.getFontSize(), new IntegerFieldValidator(4, 1000), value -> {
            descriptor.setFontSize(value);
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
    }

    public JTextField getDurationField(AdrDurationComponentDescriptor descriptor) {
        return this.getSmartField(descriptor.getDuration(), new DoubleFieldValidator(0.1, 1000.0), value -> {
            descriptor.setDuration(value);
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
    }

    public JTextField getDelayField(AdrDurationComponentDescriptor descriptor) {
        return this.getSmartField(descriptor.getDelay(), new DoubleFieldValidator(0.0, 1000.0), value -> {
            descriptor.setDelay(value);
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
    }

    public JPanel getBackgroundColorPanel(AdrDurationComponentDescriptor descriptor) {
        return this.getHexColorPickerPanel(
                descriptor::getBackgroundColor,
                color -> {
                    descriptor.setBackgroundColor(color);
                    MercuryStoreUI.adrReloadSubject.onNext(descriptor);
                });
    }

    public JPanel getTextOutlinePanel(AdrDurationComponentDescriptor descriptor) {
        JPanel textPanel = this.componentsFactory.getJPanel(new BorderLayout());
        textPanel.setBackground(AppThemeColor.SLIDE_BG);
        JTextField outlineThicknessField = this.getSmartField(descriptor.getOutlineThickness(), new FloatFieldValidator(0f, 20f), value -> {
            descriptor.setOutlineThickness(value);
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        outlineThicknessField.setPreferredSize(new Dimension(40, 20));
        JPanel outlineColor = this.getHexColorPickerPanel(
                descriptor::getOutlineColor,
                color -> {
                    descriptor.setOutlineColor(color);
                    MercuryStoreUI.adrReloadSubject.onNext(descriptor);
                });
        textPanel.add(this.componentsFactory.wrapToSlide(outlineThicknessField, AppThemeColor.ADR_BG, 6, 6, 6, 4), BorderLayout.LINE_START);
        textPanel.add(outlineColor, BorderLayout.CENTER);
        return textPanel;
    }

    public JPanel getForegroundColorPanel(AdrDurationComponentDescriptor descriptor) {
        return this.getHexColorPickerPanel(
                descriptor::getForegroundColor,
                color -> {
                    descriptor.setForegroundColor(color);
                    MercuryStoreUI.adrReloadSubject.onNext(descriptor);
                });
    }

    public JCheckBox getAlwaysVisibleBox(AdrDurationComponentDescriptor descriptor) {
        JCheckBox alwaysVisibleBox = this.componentsFactory.getCheckBox(descriptor.isAlwaysVisible());
        alwaysVisibleBox.addActionListener(action -> {
            descriptor.setAlwaysVisible(alwaysVisibleBox.isSelected());
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        return alwaysVisibleBox;
    }

    public JCheckBox getInvertTimerBox(AdrDurationComponentDescriptor descriptor) {
        JCheckBox invertTimerBox = this.componentsFactory.getCheckBox(descriptor.isInvertTimer());
        invertTimerBox.addActionListener(action -> {
            descriptor.setInvertTimer(invertTimerBox.isSelected());
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        return invertTimerBox;
    }

    public JCheckBox getInvertMaskBox(AdrDurationComponentDescriptor descriptor) {
        JCheckBox invertMaskBox = this.componentsFactory.getCheckBox(descriptor.isInvertMask());
        invertMaskBox.addActionListener(action -> {
            descriptor.setInvertMask(invertMaskBox.isSelected());
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        return invertMaskBox;
    }

    public JCheckBox getMaskEnableBox(AdrDurationComponentDescriptor descriptor) {
        JCheckBox animationBox = this.componentsFactory.getCheckBox(descriptor.isMaskEnable());
        animationBox.addActionListener(e ->
                descriptor.setMaskEnable(animationBox.isSelected()));
        return animationBox;
    }

    public JPanel getExTextColorPanel(AdrDurationComponentDescriptor descriptor) {
        JPanel textColorPanel = this.getTextColorPanel(descriptor);
        JPanel textPanel = this.componentsFactory.getJPanel(new BorderLayout());
        textPanel.setBackground(AppThemeColor.SLIDE_BG);
        JCheckBox textEnableBox = this.componentsFactory.getCheckBox(descriptor.isTextEnable(), "Is text enable?");
        textEnableBox.addActionListener(state -> {
            descriptor.setTextEnable(textEnableBox.isSelected());
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        textPanel.add(textEnableBox, BorderLayout.LINE_START);
        textPanel.add(textColorPanel, BorderLayout.CENTER);
        return textPanel;
    }

    public JPanel getBorderColorPanel(AdrDurationComponentDescriptor descriptor) {
        return this.getBorderColorPanel(
                descriptor,
                color -> {
                    descriptor.setBorderColor(color);
                    MercuryStoreUI.adrReloadSubject.onNext(descriptor);
                });
    }

    public JComboBox getPbOrientationBox(AdrProgressBarDescriptor descriptor) {
        JComboBox pbOrientation = this.componentsFactory.getComboBox(new String[]{"Horizontal", "Vertical"});
        pbOrientation.setSelectedIndex(descriptor.getOrientation().ordinal());
        pbOrientation.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.swapSize(descriptor);
                descriptor.setOrientation(AdrComponentOrientation.valueOfPretty((String) pbOrientation.getSelectedItem()));
                MercuryStoreUI.adrUpdateSubject.onNext(descriptor);
                MercuryStoreUI.adrReloadSubject.onNext(descriptor);
            }
        });
        return pbOrientation;
    }

    private void swapSize(AdrProgressBarDescriptor payload) {
        payload.setSize(new Dimension(payload.getSize().height, payload.getSize().width));
    }

    public JPanel getIconPanel(AdrDurationComponentDescriptor descriptor) {
        JPanel iconSelectPanel = this.getIconSelectPanel(descriptor);
        JPanel iconPanel = this.componentsFactory.getJPanel(new BorderLayout());
        iconPanel.setBackground(AppThemeColor.SLIDE_BG);
        JCheckBox iconEnableBox = this.componentsFactory.getCheckBox(descriptor.isIconEnable(), "Is icon enable?");
        iconEnableBox.addActionListener(state -> {
            descriptor.setIconEnable(iconEnableBox.isSelected());
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
            MercuryStoreUI.adrManagerPack.onNext(true);
        });
        iconPanel.add(iconEnableBox, BorderLayout.LINE_START);
        iconPanel.add(iconSelectPanel, BorderLayout.CENTER);
        return iconPanel;
    }

    public JComboBox getIconAlignment(AdrProgressBarDescriptor descriptor) {
        JComboBox iconAlignment = this.componentsFactory.getComboBox(new String[]{"Left", "Right", "Top", "Bottom"});
        iconAlignment.setSelectedItem(descriptor.getIconAlignment().asPretty());
        iconAlignment.addItemListener(e -> {
            descriptor.setIconAlignment(AdrIconAlignment.valueOfPretty((String) iconAlignment.getSelectedItem()));
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
            MercuryStoreUI.adrManagerPack.onNext(true);
        });
        return iconAlignment;
    }

    public JPanel getColorPickerPanel(ValueBinder<Color> binder, DialogCallback<Color> callback) {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 1, 6, 0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JColorChooser colorChooser = getColorChooser();
        JPanel colorPanel = new JPanel();
        colorPanel.addMouseListener(new ColorChooserMouseListener(colorPanel) {
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
        wrapper.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        wrapper.add(root, BorderLayout.CENTER);
        return wrapper;
    }

    public JPanel getHexColorPickerPanel(ValueBinder<Color> colorBinder, DialogCallback<Color> callback) {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(6, 0));
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
        hexField.setPreferredSize(new Dimension(76, 26));
        colorPanel.addMouseListener(new ColorChooserMouseListener(colorPanel) {
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

        root.add(hexField, BorderLayout.LINE_START);
        root.add(colorPanel, BorderLayout.CENTER);

        JPanel wrapper = this.componentsFactory.getJPanel(new BorderLayout());
        wrapper.setBackground(AppThemeColor.SLIDE_BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        wrapper.add(root, BorderLayout.CENTER);
        return wrapper;
    }

    public <T> JTextField getSmartField(T value, FieldValidator<String, T> validator, FieldValueListener<T> listener) {
        JTextField field = this.componentsFactory.getTextField(String.valueOf(value), FontStyle.REGULAR, 16f);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (validator.validate(field.getText())) {
                    listener.onAction(validator.getValue());
                } else {
                    field.setText(String.valueOf(value));
                }
            }
        });
        field.addActionListener(action -> {
            if (validator.validate(field.getText())) {
                listener.onAction(validator.getValue());
            } else {
                field.setText(String.valueOf(value));
            }
        });
        return field;
    }

    public JPanel getBorderColorPanel(AdrDurationComponentDescriptor descriptor, DialogCallback<Color> callback) {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(AppThemeColor.SLIDE_BG);
        JCheckBox checkBox = this.componentsFactory.getCheckBox(descriptor.isBindToTextColor(), "Bind to text color?");
        checkBox.addActionListener(e -> descriptor.setBindToTextColor(checkBox.isSelected()));
        JPanel colorPickerPanel = this.getHexColorPickerPanel(descriptor::getBorderColor, value -> {
            checkBox.setSelected(false);
            callback.onAction(value);
        });

        root.add(checkBox, BorderLayout.LINE_START);
        root.add(colorPickerPanel, BorderLayout.CENTER);
        return root;
    }

    public JPanel getGapPanel(AdrTrackerGroupDescriptor descriptor) {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 4, 4, 0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel hGap = this.componentsFactory.getTextLabel("hGap:");
        JLabel vGap = this.componentsFactory.getTextLabel("vGap:");
        JTextField hGapField = this.getSmartField(descriptor.getHGap(), new IntegerFieldValidator(0, 200), value -> {
            descriptor.setHGap(value);
            MercuryStoreUI.adrReloadSubject.onNext(descriptor);
        });
        JTextField vGapField = this.getSmartField(descriptor.getVGap(), new IntegerFieldValidator(0, 200), value -> {
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

        if (treeNode.getParent().getData() != null) {
            JMenuItem moveOut = this.componentsFactory.getMenuItem("Move out");
            moveOut.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppThemeColor.ADR_DEFAULT_BORDER));
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
            if (neighbor.getData() instanceof AdrTrackerGroupDescriptor) {
                JMenuItem moveTo = this.componentsFactory.getMenuItem("Move to " + neighbor.getData().getTitle());
                moveTo.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppThemeColor.ADR_DEFAULT_BORDER));
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
                switch (((AdrTrackerGroupDescriptor) neighbor.getData()).getContentType()) {
                    case ICONS: {
                        if (treeNode.getData() instanceof AdrIconDescriptor) {
                            contextMenu.add(moveTo);
                        }
                        break;
                    }
                    case PROGRESS_BARS: {
                        if (treeNode.getData() instanceof AdrProgressBarDescriptor) {
                            contextMenu.add(moveTo);
                        }
                        break;
                    }
                }
            }
        });
        JMenuItem export = this.componentsFactory.getMenuItem("Share");
        export.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppThemeColor.ADR_DEFAULT_BORDER));
        export.addActionListener(action -> {
            MercuryStoreUI.adrExportSubject.onNext(treeNode.getData());
        });
        contextMenu.add(export);
        return contextMenu;
    }

    public JPanel getRightComponentOperationsPanel(AdrComponentDescriptor descriptor) {
        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 14, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_REMOVE_BUTTON);
        removeButton.addActionListener(action -> {
            new AlertDialog(this.getRemoveCallback(descriptor), "Do you want to delete \"" + descriptor.getTitle() + "\"component?", removeButton).setVisible(true);
        });
        JButton visibleButton = this.componentsFactory.getIconButton("app/adr/visible_node_on.png", 14, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_VISIBLE_BUTTON);
        visibleButton.addActionListener(action -> {
            if (descriptor.isVisible()) {
                visibleButton.setIcon(this.componentsFactory.getIcon("app/adr/visible_node_off.png", 14));
                descriptor.setVisible(false);
            } else {
                visibleButton.setIcon(this.componentsFactory.getIcon("app/adr/visible_node_on.png", 14));
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
        JButton duplicateButton = this.componentsFactory.getIconButton("app/adr/duplicate_node.png", 14, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_DUPLICATE_BUTTON);
        duplicateButton.addActionListener(action -> {
            AdrComponentDescriptor cloned = CloneHelper.cloneObject(treeNode.getData());
            if (cloned != null) {
                cloned.setComponentId(UUID.randomUUID().toString());
                cloned.setLocation(new Point(new Random().nextInt(500), new Random().nextInt(500)));
                MercuryStoreUI.adrComponentStateSubject.onNext(
                        new AdrComponentDefinition(cloned,
                                AdrComponentOperations.NEW_COMPONENT,
                                treeNode.getParent().getData()));
            }
        });
        JButton moveButton = this.componentsFactory.getIconButton("app/adr/move_node.png", 14, AppThemeColor.SLIDE_BG, TooltipConstants.ADR_MOVE_BUTTON);
        moveButton.addActionListener(action -> {
            JPopupMenu contextMenu = this.getContextMenu(treeNode);
            contextMenu.show(moveButton, 8, 8);
        });
        JPanel buttonsPanel = this.componentsFactory.getJPanel(new GridLayout(2, 2));
        buttonsPanel.setBackground(AppThemeColor.SLIDE_BG);
        buttonsPanel.add(moveButton);
        buttonsPanel.add(duplicateButton);
        return buttonsPanel;
    }

    public JPanel getCounterPanel(JPanel innerPanel, String title, Color bg, boolean initial) {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(bg);

        JPanel headerPanel = this.componentsFactory.getJPanel(new BorderLayout());
        headerPanel.setBackground(bg);
        JLabel titleLabel = this.componentsFactory.getTextLabel(title);
        JButton counterButton = this.getCounterButton(innerPanel, bg, initial);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(counterButton, BorderLayout.LINE_START);
        root.add(headerPanel, BorderLayout.PAGE_START);
        root.add(innerPanel, BorderLayout.CENTER);
        return root;
    }

    public JButton getCounterButton(JPanel targetPanel, Color bg, boolean initial) {
        String initialIcon = (initial) ? "app/adr/collapse_icon.png" : "app/adr/expand_icon.png";
        JButton expandButton = this.componentsFactory.getIconButton(initialIcon, 16, AppThemeColor.FRAME, "");
        expandButton.setBackground(bg);
        expandButton.addActionListener(action -> {
            if (targetPanel.isVisible()) {
                expandButton.setIcon(this.componentsFactory.getIcon("app/adr/expand_icon.png", 16));
                targetPanel.setVisible(false);
            } else {
                expandButton.setIcon(this.componentsFactory.getIcon("app/adr/collapse_icon.png", 16));
                targetPanel.setVisible(true);
            }
            MercuryStoreUI.adrManagerRepaint.onNext(true); //todo
        });
        return expandButton;
    }

    public String getGroupTypeIconPath(AdrTrackerGroupDescriptor descriptor) {
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

    public DialogCallback<Boolean> getRemoveCallback(AdrComponentDescriptor descriptor) {
        return success -> {
            if (success) {
                MercuryStoreUI.adrRemoveComponentSubject.onNext(descriptor);
                MercuryStoreUI.adrManagerPack.onNext(true);
            }
        };
    }

    private JColorChooser getColorChooser() {
        JColorChooser colorChooser = new JColorChooser();
        String type = UIManager.getString("ColorChooser.hsvNameText", colorChooser.getLocale());
        for (AbstractColorChooserPanel p : colorChooser.getChooserPanels()) {
            if (!p.getDisplayName().equals(type)) {
                colorChooser.removeChooserPanel(p);
            }
        }
        return colorChooser;
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
