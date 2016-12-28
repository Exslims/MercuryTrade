package com.mercury.platform.ui;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.NewWhispersEvent;
import com.mercury.platform.shared.events.custom.RepaintEvent;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.fields.ExScrolBarUI;
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
    private final int SCROLL_HEIGHT = 600;

    public HistoryFrame() {
        super("History");
    }

    @Override
    protected void init() {
        super.init();
        disableHideEffect(); // todo
        this.setMinimumSize(new Dimension(400,100));
        messagesContainer = new JPanel();
        messagesContainer.setBackground(AppThemeColor.TRANSPARENT);
        messagesContainer.setLayout(new BoxLayout(messagesContainer,BoxLayout.Y_AXIS));
        add(getTopPanel(),BorderLayout.PAGE_START);

        scrollPane = new JScrollPane(messagesContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(AppThemeColor.FRAME);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                HistoryFrame.this.repaint();
            }
        });
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setBackground(AppThemeColor.FRAME);
        vBar.setUI(new ExScrolBarUI());
        vBar.setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
        vBar.setUnitIncrement(3);
        vBar.setBorder(BorderFactory.createLineBorder(AppThemeColor.FRAME,1));
        vBar.addAdjustmentListener(e -> HistoryFrame.this.repaint());

        add(scrollPane,BorderLayout.CENTER);
        pack();
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
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
                HistoryFrame.this.setSize(new Dimension(400,100));
                scrollPane.setSize(new Dimension(messagesContainer.getSize().width, messagesContainer.getSize().height));
                scrollPane.setPreferredSize(null);
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
                if(messagesContainer.getComponentCount() > 0){
                    messagePanel.setBorder(BorderFactory.createMatteBorder(1,0,0,0, AppThemeColor.BORDER));
                }
                messagesContainer.add(messagePanel);
            }
            if(this.getSize().height > SCROLL_HEIGHT) {
                this.pack();
                scrollPane.setPreferredSize(new Dimension(messagesContainer.getWidth(), SCROLL_HEIGHT));
                scrollPane.setSize(new Dimension(messagesContainer.getWidth(), SCROLL_HEIGHT));
                messagesContainer.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
            }else {
                scrollPane.setSize(new Dimension(messagesContainer.getWidth(), this.getSize().height));
            }
            this.pack();
            this.repaint();
        });
        EventRouter.registerHandler(RepaintEvent.class, event -> {
            this.revalidate();
            this.repaint();
        });
    }
}
