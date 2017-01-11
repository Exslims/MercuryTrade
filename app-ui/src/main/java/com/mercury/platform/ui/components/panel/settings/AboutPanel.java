package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.HasUI;

import java.awt.*;

/**
 * Created by Константин on 05.01.2017.
 */
public class AboutPanel extends ConfigurationPanel implements HasUI {
    public AboutPanel() {
        super();
        createUI();
    }

    @Override
    public void processAndSave() {

    }

    @Override
    public void createUI() {
        this.add(componentsFactory.getTextLabel("About program and contacts here."));
        this.add(componentsFactory.getTextField("TYPE HERE", FontStyle.BOLD,16));
    }

    @Override
    protected LayoutManager getPanelLayout() {
        return new FlowLayout(FlowLayout.LEFT);
    }
}
