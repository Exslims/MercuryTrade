package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.frame.impl.NotesFrame;
import com.mercury.platform.ui.frame.impl.SettingsFrame;
import com.mercury.platform.ui.frame.impl.test.TestCasesFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 26.02.2017.
 */
public class HelpPanel extends JPanel implements HasUI {
    private ComponentsFactory componentsFactory;
    public HelpPanel() {
        super();
        componentsFactory = new ComponentsFactory();
        this.setBackground(AppThemeColor.TRANSPARENT);
        createUI();
    }

    @Override
    public void createUI() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton openTutorial = componentsFactory.getBorderedButton("Open tutorial");
        openTutorial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    FramesManager.INSTANCE.hideFrame(SettingsFrame.class);
                    FramesManager.INSTANCE.showFrame(NotesFrame.class);
                }
            }
        });
        JButton openTests = componentsFactory.getBorderedButton("Open tests");
        openTests.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    FramesManager.INSTANCE.hideFrame(SettingsFrame.class);
                    FramesManager.INSTANCE.showFrame(TestCasesFrame.class);
                }
            }
        });
        this.add(openTutorial);
        this.add(openTests);
    }
}
