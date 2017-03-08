package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.core.misc.WhisperNotifierStatus;
import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.AlertEvent;
import com.mercury.platform.shared.events.custom.CheckOutPatchNotes;
import com.mercury.platform.shared.events.custom.ClosingPatchNotesEvent;
import com.mercury.platform.shared.events.custom.RequestPatchNotesEvent;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.frame.ComponentFrame;
import com.mercury.platform.ui.manager.HideSettingsManager;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 05.01.2017.
 */
public class GeneralSettings extends ConfigurationPanel implements HasUI {
    private JSlider minSlider;
    private JSlider maxSlider;
    private JComboBox secondsPicker;
    private JComboBox notifierStatusPicker;
    private ComponentFrame owner;
    private JButton checkUpdate;
    private JCheckBox checkEnable;
    public GeneralSettings(ComponentFrame owner) {
        super();
        this.owner = owner;
        createUI();

        EventRouter.INSTANCE.registerHandler(AlertEvent.class, event -> {
            checkUpdate.setEnabled(true);
        });
        EventRouter.INSTANCE.registerHandler(ClosingPatchNotesEvent.class, event -> {
            checkUpdate.setEnabled(true);
        });
    }

    @Override
    protected LayoutManager getPanelLayout() {
        return new BorderLayout();
    }

    @Override
    public void createUI() {
        this.add(getSettingsPanel(),BorderLayout.PAGE_START);
    }
    private JPanel getSettingsPanel() {
        JPanel root = componentsFactory.getTransparentPanel(new GridBagLayout());

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridy = 0;
        constraint.gridx = 0;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.weightx = 0.7f;
        constraint.insets = new Insets(0,0,0,5);

        JPanel updatePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        updatePanel.add(componentsFactory.getTextLabel("Notify me when an update is available", FontStyle.REGULAR));
        checkEnable = new JCheckBox();
        checkEnable.setBackground(AppThemeColor.TRANSPARENT);
        checkEnable.setSelected(ConfigManager.INSTANCE.isCheckUpdateOnStartUp());
        updatePanel.add(checkEnable);

        JPanel checkUpdatesPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        checkUpdate = componentsFactory.getBorderedButton("Check for updates");
        checkUpdate.addActionListener(action -> {
            checkUpdate.setEnabled(false);
            ApplicationHolder.getInstance().setManualRequest(true);
            EventRouter.INSTANCE.fireEvent(new RequestPatchNotesEvent());
        });
        checkUpdatesPanel.add(checkUpdate);

        JPanel hideSettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        hideSettingsPanel.add(componentsFactory.getTextLabel("Fade time (seconds). 0 - Always show", FontStyle.REGULAR));

        secondsPicker = componentsFactory.getComboBox(new String[]{"0","1","2","3","4","5"});
        int decayTime = ConfigManager.INSTANCE.getDecayTime();
        secondsPicker.setSelectedIndex(decayTime);

        JPanel minOpacitySettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        minOpacitySettingsPanel.add(componentsFactory.getTextLabel("Min opacity: ", FontStyle.REGULAR));

        JPanel minValuePanel = componentsFactory.getTransparentPanel(new FlowLayout());
        JLabel minValueField = componentsFactory.getTextLabel(ConfigManager.INSTANCE.getMinOpacity() + "%", FontStyle.REGULAR); //todo
        minValuePanel.add(minValueField);
        minValuePanel.setPreferredSize(new Dimension(35,30));
        minOpacitySettingsPanel.add(minValuePanel);

        minSlider = componentsFactory.getSlider(10,100,ConfigManager.INSTANCE.getMinOpacity());
        minSlider.addChangeListener(e -> {
            if(!(minSlider.getValue() > maxSlider.getValue())) {
                minValueField.setText(String.valueOf(minSlider.getValue()) + "%");
                owner.repaint();
            }else {
                minSlider.setValue(minSlider.getValue()-1);
            }
        });

        JPanel maxOpacitySettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        maxOpacitySettingsPanel.add(componentsFactory.getTextLabel("Max opacity: ", FontStyle.REGULAR));

        JPanel maxValuePanel = componentsFactory.getTransparentPanel(new FlowLayout());
        JLabel maxValueField = componentsFactory.getTextLabel(ConfigManager.INSTANCE.getMaxOpacity() + "%", FontStyle.REGULAR); //todo
        maxValuePanel.add(maxValueField);
        maxValuePanel.setPreferredSize(new Dimension(35,30));
        maxOpacitySettingsPanel.add(maxValuePanel);

        maxSlider = componentsFactory.getSlider(20,100,ConfigManager.INSTANCE.getMaxOpacity());
        maxSlider.addChangeListener(e -> {
            maxValueField.setText(String.valueOf(maxSlider.getValue()) + "%");
            owner.setOpacity(maxSlider.getValue()/100.0f);
        });
        maxSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(minSlider.getValue() > maxSlider.getValue()){
                    minSlider.setValue(maxSlider.getValue());
                }
            }
        });

        JPanel notifierPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        notifierPanel.add(componentsFactory.getTextLabel("Notification sound alerts: ", FontStyle.REGULAR));
        notifierStatusPicker = componentsFactory.getComboBox(new String[]{"Always play a sound", "Only when tabbed out","Never"});
        WhisperNotifierStatus whisperNotifier = ConfigManager.INSTANCE.getWhisperNotifier();
        notifierStatusPicker.setSelectedIndex(whisperNotifier.getCode());

        root.add(updatePanel,constraint);
        constraint.gridy = 1;
        root.add(hideSettingsPanel,constraint);
        constraint.gridy = 2;
        root.add(minOpacitySettingsPanel,constraint);
        constraint.gridy = 3;
        root.add(maxOpacitySettingsPanel,constraint);
        constraint.gridy = 4;
        root.add(notifierPanel,constraint);
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.weightx = 0.3f;
        root.add(checkUpdatesPanel,constraint);
        constraint.gridy = 1;
        root.add(secondsPicker,constraint);
        constraint.gridy = 2;
        root.add(minSlider,constraint);
        constraint.gridy = 3;
        root.add(maxSlider,constraint);
        constraint.gridy = 4;
        root.add(notifierStatusPicker,constraint);
        return root;
    }
    @Override
    public void processAndSave() {
        int timeToDelay = Integer.parseInt((String) secondsPicker.getSelectedItem());
        int minOpacity = minSlider.getValue();
        int maxOpacity = maxSlider.getValue();
        HideSettingsManager.INSTANCE.apply(timeToDelay,minOpacity,maxOpacity);
        ConfigManager.INSTANCE.setWhisperNotifier(WhisperNotifierStatus.get(notifierStatusPicker.getSelectedIndex()));
        ConfigManager.INSTANCE.setCheckUpdateOnStartUp(checkEnable.isSelected());
    }

    @Override
    public void restore() {
        this.removeAll();
        this.createUI();
        owner.setOpacity(maxSlider.getValue()/100.0f);
    }
}
