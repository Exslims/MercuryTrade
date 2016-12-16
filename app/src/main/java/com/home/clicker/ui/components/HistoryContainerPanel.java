package com.home.clicker.ui.components;

import com.home.clicker.events.EventRouter;
import com.home.clicker.events.custom.MoveToHistoryEvent;
import com.home.clicker.events.custom.OpenHistoryEvent;

import javax.swing.*;
import java.awt.*;

/**
 * todo vinesti nahui v abstract
 */
public class HistoryContainerPanel extends TransparencyContainerPanel{
    public HistoryContainerPanel() {
        super(new Dimension(600,500));
    }

    @Override
    protected void init() {
        super.init();
        title.setText("History");
    }

    @Override
    protected void initHandlers() {
        EventRouter.registerHandler(OpenHistoryEvent.class, event -> {
            if(!HistoryContainerPanel.this.isVisible()) {
                int x = ((OpenHistoryEvent) event).getX();
                int y = ((OpenHistoryEvent) event).getY();
                HistoryContainerPanel.this.setLocation(x, y);
                HistoryContainerPanel.this.setVisible(true);
            }else{
                HistoryContainerPanel.this.setVisible(false);
            }
        });
        EventRouter.registerHandler(MoveToHistoryEvent.class, event -> {
            JPanel messagePanel = ((MoveToHistoryEvent) event).getMessagePanel();
            messagePanel.setMaximumSize(new Dimension(size.width,100));
            messagePanel.setPreferredSize(new Dimension(size.width,100));
            messagePanel.setMinimumSize(new Dimension(size.width,100));
            container.add(messagePanel);
            container.revalidate();
            container.repaint();
            container.scrollRectToVisible(new Rectangle(0, container.getPreferredSize().height-1,1,1));
        });
    }
}
