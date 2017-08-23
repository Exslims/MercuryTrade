package com.mercury.platform.ui.components.panel;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;


public abstract class TransparentPanel extends JPanel {
    protected ComponentsFactory componentsFactory;
    protected VerticalScrollContainer verticalScrollContainer;

    public TransparentPanel() {
        this.componentsFactory = new ComponentsFactory();

        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.SLIDE_BG);
        this.setBorder(null);
        this.verticalScrollContainer = new VerticalScrollContainer();
        verticalScrollContainer.setBackground(AppThemeColor.SLIDE_BG);
        verticalScrollContainer.setLayout(new BoxLayout(verticalScrollContainer, BoxLayout.Y_AXIS));


        JScrollPane scrollPane = new JScrollPane(verticalScrollContainer);
        scrollPane.setBackground(AppThemeColor.FRAME);
        scrollPane.getViewport().setBackground(AppThemeColor.SLIDE_BG);
        scrollPane.getViewport().setBorder(null);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                TransparentPanel.this.repaint();
            }
        });
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setBackground(AppThemeColor.SLIDE_BG);
        vBar.setUI(new MercuryScrollBarUI());
        vBar.setPreferredSize(new Dimension(14, Integer.MAX_VALUE));
        vBar.setUnitIncrement(3);
        vBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 2));
        vBar.addAdjustmentListener(e -> repaint());
        this.add(scrollPane, BorderLayout.CENTER);

    }
}
