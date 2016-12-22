package com.home.clicker.ui.components.panel;

import com.home.clicker.shared.events.EventRouter;
import com.home.clicker.shared.events.custom.MoveToHistoryEvent;
import com.home.clicker.shared.events.custom.NewWhispersEvent;
import com.home.clicker.shared.events.custom.OpenHistoryEvent;
import com.home.clicker.shared.events.custom.RepaintEvent;
import com.home.clicker.shared.pojo.Message;
import com.home.clicker.ui.components.fields.ExButton;
import com.home.clicker.ui.misc.AppThemeColor;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class MessagesContainerPanel extends TransparencyContainerPanel {
    public MessagesContainerPanel() {
        super(new Dimension(350,200));
    }

    @Override
    protected void init() {
        super.init();

        BufferedImage buttonIcon = null;
        try {
            buttonIcon = ImageIO.read(getClass().getClassLoader().getResource("app/history.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage icon = Scalr.resize(buttonIcon, 15);
        ExButton expandHistory = new ExButton(new ImageIcon(icon));
        headButtonsPanel.add(expandHistory,BorderLayout.CENTER);

        expandHistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventRouter.fireEvent(new OpenHistoryEvent());
            }
        });
    }

    @Override
    public void initHandlers() {
        EventRouter.registerHandler(NewWhispersEvent.class, event -> {
            List<Message> messages = ((NewWhispersEvent) event).getMessages();
            for (Message message : messages) {
                MessagePanel messagePanel = new MessagePanel(message.getWhisperNickname(), message.getMessage());
                messagePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        messagePanel.viewed();
//                        if(hideTimer.isRunning()){
//                            hideTimer.stop();
//                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
//                        if(!hideTimer.isRunning()){
//                            hideTimer.start();
//                        }
                    }
                });
                if(container.getComponentCount() != 0) {
                    Component prevMessage = container.getComponent(0);
                    EventRouter.fireEvent(new MoveToHistoryEvent((JPanel) prevMessage));
                    container.removeAll();
                }
                container.add(messagePanel);
                EventRouter.fireEvent(new RepaintEvent());
            }
            container.revalidate();
            container.repaint();
            container.scrollRectToVisible(new Rectangle(0, container.getPreferredSize().height-1,1,1));
            MessagesContainerPanel.this.setVisible(true);
//            if(hideTimer.isRunning()){
//                hideTimer.stop();
//            }
//            hideTimer.start();
        });
    }
    public static class ExScrollBarUI extends BasicScrollBarUI{
        private final Dimension d = new Dimension();

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return new JButton() {
                @Override
                public Dimension getPreferredSize() {
                    return d;
                }
            };
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new JButton() {
                @Override
                public Dimension getPreferredSize() {
                    return d;
                }
            };
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            Color color = null;
            JScrollBar sb = (JScrollBar) c;
            if (!sb.isEnabled() || r.width > r.height) {
                return;
            } else {
                color = AppThemeColor.BUTTON;
            }
            g2.setPaint(color);
            g2.fillRect(r.x, r.y, r.width, r.height);
            g2.setPaint(AppThemeColor.TRANSPARENT);
            g2.drawRect(r.x, r.y, r.width, r.height);
            g2.dispose();
        }

        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
            super.setThumbBounds(x, y, width, height);
            scrollbar.repaint();
        }
    }
}
