package com.home.clicker.ui.components;

import com.home.clicker.shared.events.EventRouter;
import com.home.clicker.shared.events.custom.MoveToHistoryEvent;
import com.home.clicker.shared.events.custom.OpenHistoryEvent;
import com.home.clicker.shared.events.custom.RepaintEvent;
import com.home.clicker.ui.components.fields.ExButton;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

        BufferedImage buttonIcon = null;
        try {
            buttonIcon = ImageIO.read(getClass().getClassLoader().getResource("clear-icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage icon = Scalr.resize(buttonIcon, 15);
        ExButton clearHistory = new ExButton(new ImageIcon(icon));
        headButtonsPanel.add(clearHistory,BorderLayout.CENTER);

        clearHistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                container.removeAll();
                EventRouter.fireEvent(new RepaintEvent());
            }
        });
    }

    @Override
    public void initHandlers() {
        EventRouter.registerHandler(OpenHistoryEvent.class, event -> {
            if(!HistoryContainerPanel.this.isVisible()) {
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
            container.scrollRectToVisible(new Rectangle(0, container.getPreferredSize().height-1,1,1));
            container.revalidate();
            container.repaint();
        });
    }
}
