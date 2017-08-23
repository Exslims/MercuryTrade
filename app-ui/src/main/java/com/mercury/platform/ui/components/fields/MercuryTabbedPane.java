package com.mercury.platform.ui.components.fields;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MercuryTabbedPane extends JPanel {
    private ComponentsFactory componentsFactory;
    private List<JButton> headerButtons;

    private JFrame owner;
    private JPanel headersPanel;
    private JPanel tabPanel;

    public MercuryTabbedPane(JFrame owner) {
        this.owner = owner;
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setLayout(new BorderLayout());

        headerButtons = new ArrayList<>();
        componentsFactory = new ComponentsFactory();
        headersPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        headersPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, -5, 0));

        tabPanel = componentsFactory.getTransparentPanel(new BorderLayout());

        this.add(headersPanel, BorderLayout.PAGE_START);
        this.add(tabPanel, BorderLayout.CENTER);
    }

    public void addTab(String title, JPanel component) {
        component.setBackground(AppThemeColor.SLIDE_BG);
        JButton tabButton = componentsFactory.getButton(
                FontStyle.BOLD,
                AppThemeColor.TRANSPARENT,
                BorderFactory.createMatteBorder(1, 1, 0, 1, AppThemeColor.HEADER),
                title, 15f);
        tabButton.setPreferredSize(new Dimension(tabButton.getPreferredSize().width + 14, 30));
        tabButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    tabButton.setBackground(AppThemeColor.SLIDE_BG);

                    headerButtons.forEach(button -> {
                        if (!button.equals(tabButton)) {
                            button.setBackground(AppThemeColor.TRANSPARENT);
                        }
                    });
                    tabPanel.removeAll();
                    tabPanel.add(component, BorderLayout.CENTER);
                    owner.pack();
                    owner.repaint();
                }
            }
        });
        if (headerButtons.size() == 0) {
            tabButton.setBackground(AppThemeColor.SLIDE_BG);
            tabPanel.add(component, BorderLayout.CENTER);
        }

        headerButtons.add(tabButton);
        headersPanel.add(tabButton);
    }
}
