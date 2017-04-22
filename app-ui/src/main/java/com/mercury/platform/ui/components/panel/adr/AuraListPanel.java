package com.mercury.platform.ui.components.panel.adr;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.event.RepaintEvent;
import com.mercury.platform.ui.misc.event.ScrollToTheEndEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

public class AuraListPanel extends JPanel implements HasUI{
    private ComponentsFactory componentsFactory;
    private JPanel container;

    public AuraListPanel(ComponentsFactory factory) {
        this.componentsFactory = factory;
        createUI();
    }

    @Override
    public void createUI() {
        container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.TRANSPARENT);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));

        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setBorder(BorderFactory.createMatteBorder(0,0,0,1,AppThemeColor.BORDER));

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.setBackground(AppThemeColor.FRAME);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                EventRouter.UI.fireEvent(new RepaintEvent.RepaintChatFilter());
            }
        });
        scrollPane.addMouseWheelListener(e -> {
            EventRouter.UI.fireEvent(new ScrollToTheEndEvent(false));
        });

        container.getParent().setBackground(AppThemeColor.TRANSPARENT);
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setBackground(AppThemeColor.SLIDE_BG);
        vBar.setUI(new MercuryScrollBarUI());
        vBar.setPreferredSize(new Dimension(15, Integer.MAX_VALUE));
        vBar.setUnitIncrement(3);
        vBar.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        vBar.addAdjustmentListener(e -> {
            EventRouter.UI.fireEvent(new RepaintEvent.RepaintChatFilter());
        });

        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.add(scrollPane);
        this.setMaximumSize(new Dimension(100,Integer.MAX_VALUE));
        this.createTestCells();
    }

    public void createTestCells(){
        for (int i = 0; i < 5; i++) {
            JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
            root.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
            root.add(componentsFactory.getTextLabel("Test " + i));
            container.add(root);
        }
    }
}
