package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.ui.components.panel.HasUI;
import com.mercury.platform.ui.frame.ComponentFrame;
import com.mercury.platform.ui.manager.HideSettingsManager;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 05.01.2017.
 */
public class GeneralSettings extends ConfigurationPanel implements HasUI {
    private JSlider minSlider;
    private JSlider maxSlider;
    private JComboBox secondsPicker;
    private ComponentFrame owner;

    private HideSettingsManager hideManager = HideSettingsManager.INSTANCE;

    public GeneralSettings(ComponentFrame owner) {
        super();
        this.owner = owner;
        createUI();
    }

    @Override
    protected LayoutManager getPanelLayout() {
        return new BoxLayout(this,BoxLayout.Y_AXIS);
    }

    @Override
    public void createUI() {
        secondsPicker = componentsFactory.getComboBox(new String[]{"0","1","2","3","4","5"});

        JPanel hideSettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        hideSettingsPanel.add(componentsFactory.getTextLabel("Fade time:"));
        hideSettingsPanel.add(secondsPicker);
        hideSettingsPanel.add(componentsFactory.getTextLabel("sec. 0 - always show"));

        JPanel minOpacitySettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        minOpacitySettingsPanel.add(componentsFactory.getTextLabel("Min opacity: "));

        JPanel minValuePanel = componentsFactory.getTransparentPanel(new FlowLayout());
        JLabel minValueField = componentsFactory.getTextLabel(hideManager.getMinOpacity() + "%"); //todo
        minValuePanel.add(minValueField);
        minValuePanel.setPreferredSize(new Dimension(35,30));
        minOpacitySettingsPanel.add(minValuePanel);

        minSlider = componentsFactory.getSlider(10,100,hideManager.getMinOpacity());
        minSlider.addChangeListener(e -> {
            minValueField.setText(String.valueOf(minSlider.getValue()) + "%");
            owner.repaint();
        });
        minOpacitySettingsPanel.add(minSlider);

        JPanel maxOpacitySettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        maxOpacitySettingsPanel.add(componentsFactory.getTextLabel("Max opacity: "));

        JPanel maxValuePanel = componentsFactory.getTransparentPanel(new FlowLayout());
        JLabel maxValueField = componentsFactory.getTextLabel(hideManager.getMaxOpacity() + "%"); //todo
        maxValuePanel.add(maxValueField);
        maxValuePanel.setPreferredSize(new Dimension(35,30));
        maxOpacitySettingsPanel.add(maxValuePanel);

        maxSlider = componentsFactory.getSlider(20,100,hideManager.getMaxOpacity());
        maxSlider.addChangeListener(e -> {
            maxValueField.setText(String.valueOf(maxSlider.getValue()) + "%");
            owner.setOpacity(maxSlider.getValue()/100.0f);
        });
        maxOpacitySettingsPanel.add(maxSlider);

        JPanel notifierPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        notifierPanel.add(componentsFactory.getTextLabel("Trade messages notifier: "));
        JComboBox notifierStatusPicker = componentsFactory.getComboBox(new String[]{"Always", "While on al-tab","Never"});
        notifierPanel.add(notifierStatusPicker);

        this.add(hideSettingsPanel);
        this.add(minOpacitySettingsPanel);
        this.add(maxOpacitySettingsPanel);
        this.add(notifierPanel);
    }
    @Override
    public void processAndSave() {
        int timeToDelay = Integer.parseInt((String) secondsPicker.getSelectedItem());
        int minOpacity = minSlider.getValue();
        int maxOpacity = maxSlider.getValue();
        HideSettingsManager.INSTANCE.apply(timeToDelay,minOpacity,maxOpacity);
    }
}
