package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.ui.components.fields.label.FontStyle;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 05.01.2017.
 */
public class AboutPanel extends ConfigurationPanel {
    public AboutPanel() {
        super();
    }

    @Override
    public void processAndSave() {

    }

    @Override
    protected void createUI() {
        this.add(componentsFactory.getTextLabel("About program and contacts here."));
        this.add(componentsFactory.getTextField("TYPE HERE", FontStyle.BOLD,16));
    }

    @Override
    protected LayoutManager getPanelLayout() {
        return new FlowLayout(FlowLayout.LEFT);
    }
}
