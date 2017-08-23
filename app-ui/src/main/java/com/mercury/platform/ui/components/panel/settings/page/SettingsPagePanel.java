package com.mercury.platform.ui.components.panel.settings.page;


import com.mercury.platform.ui.adr.components.AdrComponentsFactory;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

public abstract class SettingsPagePanel extends JPanel implements ViewInit {
    protected ComponentsFactory componentsFactory = new ComponentsFactory();
    protected AdrComponentsFactory adrComponentsFactory = new AdrComponentsFactory(this.componentsFactory);
    protected JPanel container;

    public SettingsPagePanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.FRAME);

        this.onViewInit();
    }

    @Override
    public void onViewInit() {
        this.container = new VerticalScrollContainer();
        this.container.setBackground(AppThemeColor.FRAME);
        this.container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(this.container);
        this.add(verticalContainer, BorderLayout.CENTER);
    }

    public abstract void onSave();

    public abstract void restore();
}
