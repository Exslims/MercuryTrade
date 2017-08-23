package com.mercury.platform.ui.components.panel;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 05.01.2017.
 */
public class CollapsiblePanel<InnerPanel extends JPanel> extends JPanel {
    private ComponentsFactory componentsFactory;
    private String title;
    private InnerPanel innerPanel;
    private JFrame owner;
    private boolean expand;

    public CollapsiblePanel(String title, InnerPanel innerPanel, JFrame owner, boolean expand) {
        this.title = title;
        this.innerPanel = innerPanel;
        this.owner = owner;
        this.expand = expand;
        this.componentsFactory = new ComponentsFactory();

        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, AppThemeColor.BORDER));
        this.setLayout(new BorderLayout());
        createUI();
    }

    private void createUI() {
        JPanel header = componentsFactory.getBorderedTransparentPanel(BorderFactory.createEmptyBorder(-4, 0, -4, 0), new FlowLayout(FlowLayout.LEFT));

        header.add(getExpandButton());
        header.add(componentsFactory.getTextLabel(title));
        header.setBackground(AppThemeColor.HEADER);
        innerPanel.setVisible(expand);
        this.add(header, BorderLayout.PAGE_START);
        this.add(innerPanel, BorderLayout.CENTER);
    }

    private JButton getExpandButton() {
        String iconPath = expand ? "app/collapse.png" : "app/expand.png";
        JButton expandButton = componentsFactory.getIconButton(iconPath, 16, AppThemeColor.FRAME_ALPHA, TooltipConstants.EXPAND_COLLAPSE);
        expandButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!innerPanel.isVisible()) {
                    expandButton.setIcon(componentsFactory.getIcon("app/collapse.png", 16));
                    innerPanel.setVisible(true);
                } else {
                    expandButton.setIcon(componentsFactory.getIcon("app/expand.png", 16));
                    innerPanel.setVisible(false);
                }
                owner.pack();
            }
        });
        return expandButton;
    }
}
