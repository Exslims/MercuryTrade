package com.mercury.platform.ui;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.NewWhispersEvent;
import com.mercury.platform.shared.events.custom.RepaintEvent;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.panel.MessagePanel;
import com.mercury.platform.ui.components.panel.MessagePanelStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Created by Константин on 27.12.2016.
 */
public class HistoryFrame extends OverlaidFrame {
    private JPanel messagesContainer;
    private JScrollPane scrollPane;

    public HistoryFrame() {
        super("History");
    }

    @Override
    protected void init() {
        super.init();
        disableHideEffect(); // todo
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(400,22));
        this.setMinimumSize(new Dimension(400,22));
        messagesContainer = new JPanel();
        messagesContainer.setBackground(AppThemeColor.TRANSPARENT);
        messagesContainer.setLayout(new BoxLayout(messagesContainer,BoxLayout.Y_AXIS));
        add(getTopPanel(),BorderLayout.PAGE_START);

        scrollPane = new JScrollPane(messagesContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(AppThemeColor.FRAME);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                HistoryFrame.this.repaint();
            }
        });
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setBackground(AppThemeColor.FRAME);
        verticalScrollBar.setUnitIncrement(2);
        verticalScrollBar.setBorder(null);
        verticalScrollBar.addAdjustmentListener(e -> HistoryFrame.this.repaint());

        add(scrollPane,BorderLayout.CENTER);
        pack();
    }
    private JPanel getTopPanel(){
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(AppThemeColor.TRANSPARENT);
        topPanel.setBorder(BorderFactory.createEmptyBorder(-6,0,-6,0));

        JLabel history = componentsFactory.getTextLabel("History");
        history.setHorizontalAlignment(SwingConstants.CENTER);
        history.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        history.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                e.translatePoint(HistoryFrame.this.getLocation().x - x,HistoryFrame.this.getLocation().y - y);
                HistoryFrame.this.setLocation(e.getX(),e.getY());
                configManager.saveComponentLocation(HistoryFrame.this.getClass().getSimpleName(),HistoryFrame.this.getLocation());
            }
        });
        topPanel.add(history,BorderLayout.CENTER);

        JPanel miscPanel = new JPanel();
        miscPanel.setBackground(AppThemeColor.TRANSPARENT);

        JButton clearButton = componentsFactory.getIconButton("app/clear-icon.png", 12);
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                messagesContainer.removeAll();
                HistoryFrame.this.setSize(new Dimension(400,22));
                scrollPane.setSize(new Dimension(HistoryFrame.this.getSize().width, HistoryFrame.this.getSize().height));
                HistoryFrame.this.pack();
            }
        });
        JButton hideButton = componentsFactory.getIconButton("app/close.png",12);
        hideButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                HistoryFrame.this.setVisible(false);
            }
        });
        miscPanel.add(clearButton);
        miscPanel.add(hideButton);
        topPanel.add(miscPanel,BorderLayout.LINE_END);
        return topPanel;
    }

    @Override
    public void initHandlers() {
        EventRouter.registerHandler(NewWhispersEvent.class, event -> {
            List<Message> messages = ((NewWhispersEvent) event).getMessages();
            for (Message message : messages) {
                MessagePanel messagePanel = new MessagePanel(message.getWhisperNickname(), message.getMessage(), MessagePanelStyle.HISTORY);
                messagesContainer.add(messagePanel);
                if(this.getSize().height > 600) {
                    scrollPane.setPreferredSize(new Dimension(this.getSize().width, 600));
                    scrollPane.setSize(new Dimension(this.getSize().width, 600));
                }else {
                    scrollPane.setSize(new Dimension(this.getSize().width, this.getSize().height));
                }
                this.repaint();
                this.pack();
            }
        });
        EventRouter.registerHandler(RepaintEvent.class, event -> {
            this.revalidate();
            this.repaint();
        });
    }
}
