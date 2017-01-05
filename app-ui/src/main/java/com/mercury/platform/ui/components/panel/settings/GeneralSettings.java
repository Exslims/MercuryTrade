package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 05.01.2017.
 */
public class GeneralSettings extends ConfigurationPanel{
    private JFrame owner;

    public GeneralSettings(JFrame owner) {
        super();
        this.owner = owner;
    }

    @Override
    protected LayoutManager getPanelLayout() {
        return new BoxLayout(this,BoxLayout.Y_AXIS);
    }

    @Override
    protected void createUI() {
        JComboBox secondsPicker = componentsFactory.getComboBox(new String[]{"0","1","2","3","4","5"});

        JPanel hideSettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        hideSettingsPanel.add(componentsFactory.getTextLabel("Hide to minimum opacity after:"));
        hideSettingsPanel.add(secondsPicker);
        hideSettingsPanel.add(componentsFactory.getTextLabel("seconds. 0 - always show"));

        JPanel minOpacitySettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        minOpacitySettingsPanel.add(componentsFactory.getTextLabel("Minimum opacity: "));

        JPanel minValuePanel = componentsFactory.getTransparentPanel(new FlowLayout());
        JLabel minValueField = componentsFactory.getTextLabel("60%"); //todo
        minValuePanel.add(minValueField);
        minValuePanel.setPreferredSize(new Dimension(35,30));
        minOpacitySettingsPanel.add(minValuePanel);

        JSlider minSlider = componentsFactory.getSlider(0,100,60);
        minSlider.addChangeListener(e -> {
            minValueField.setText(String.valueOf(minSlider.getValue()) + "%");
            owner.repaint();
        });
        minOpacitySettingsPanel.add(minSlider);

        JPanel maxOpacitySettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        maxOpacitySettingsPanel.add(componentsFactory.getTextLabel("Maximum opacity: "));

        JPanel maxValuePanel = componentsFactory.getTransparentPanel(new FlowLayout());
        JLabel maxValueField = componentsFactory.getTextLabel("60%"); //todo
        maxValuePanel.add(maxValueField);
        maxValuePanel.setPreferredSize(new Dimension(35,30));
        maxOpacitySettingsPanel.add(maxValuePanel);

        JSlider maxSlider = componentsFactory.getSlider(20,100,60);
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

    }
}
