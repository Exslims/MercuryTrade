package com.mercury.platform.ui.components.panel.adr.group;

import com.mercury.platform.shared.entity.adr.AdrIconDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.adr.ui.icon.SquareMercuryIconTrackerUI;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.pushingpixels.trident.Timeline;

import javax.swing.*;
import java.awt.*;

public class AdrGroupCellPanel extends JPanel implements HasUI{
    private ComponentsFactory componentsFactory;
    private AdrIconDescriptor descriptor;
    private Timeline progressTl;
    public AdrGroupCellPanel(AdrIconDescriptor cellDescriptor, ComponentsFactory componentsFactory) {
        super(new GridLayout(1,1));
        this.descriptor = cellDescriptor;
        this.componentsFactory = componentsFactory;
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setPreferredSize(cellDescriptor.getCellSize());
        this.setBorder(null);
        this.createUI();
    }

    @Override
    public void createUI() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setBorder(null);
        progressBar.setFont(componentsFactory.getFont(FontStyle.BOLD,36));
        progressBar.setForeground(AppThemeColor.TEXT_DEFAULT);
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(false);
        progressBar.setBackground(AppThemeColor.TRANSPARENT);
        progressBar.setUI(new SquareMercuryIconTrackerUI(descriptor.getIconPath()));
        progressBar.setValue(4000);
        progressBar.setMaximum(4000);
        add(progressBar,BorderLayout.CENTER);

        this.progressTl = new Timeline(progressBar);
        this.progressTl.setDuration((int)(descriptor.getDuration()*1000));
        this.progressTl.addPropertyToInterpolate("value",progressBar.getMaximum(),0);
        this.progressTl.playLoop(Timeline.RepeatBehavior.LOOP);
    }
}
