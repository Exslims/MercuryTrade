package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.NewWhispersEvent;
import com.mercury.platform.shared.events.custom.RepaintEvent;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.fields.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.MessagePanel;
import com.mercury.platform.ui.components.panel.MessagePanelStyle;
import com.mercury.platform.ui.frame.ComponentFrame;
import com.mercury.platform.ui.frame.TitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Константин on 27.12.2016.
 */
public class HistoryFrame extends TitledComponentFrame {
    private JPanel messagesContainer;
    private JScrollPane scrollPane;
    private final int SCROLL_HEIGHT = 600;

    public HistoryFrame() {
        super("History");
        this.setVisible(false);
        prevState = FrameStates.HIDE;
    }

    @Override
    protected void initialize() {
        super.initialize();
        messagesContainer = new JPanel();
        messagesContainer.setBackground(AppThemeColor.TRANSPARENT);
        messagesContainer.setLayout(new BoxLayout(messagesContainer,BoxLayout.Y_AXIS));
        addInteractionsButtons();

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
        vBar.setUI(new MercuryScrollBarUI());
        vBar.setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
        vBar.setUnitIncrement(3);
        vBar.setBorder(BorderFactory.createLineBorder(AppThemeColor.FRAME,1));
        vBar.addAdjustmentListener(e -> HistoryFrame.this.repaint());

        add(scrollPane,BorderLayout.CENTER);
        packFrame();
    }

    @Override
    protected String getFrameTitle() {
        return "History";
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    private void addInteractionsButtons(){
        JButton clearButton = componentsFactory.getIconButton("app/clear-icon.png", 12,AppThemeColor.FRAME_1, TooltipConstants.HISTORY_CLEAR);
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                messagesContainer.removeAll();
                HistoryFrame.this.setSize(new Dimension(400,100));
                scrollPane.setSize(new Dimension(messagesContainer.getSize().width, messagesContainer.getSize().height));
                scrollPane.setPreferredSize(null);
                HistoryFrame.this.packFrame();
            }
        });
        miscPanel.add(clearButton,0);
    }

    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(NewWhispersEvent.class, event -> {
            Message message = ((NewWhispersEvent) event).getMessage();
            MessagePanel messagePanel = new MessagePanel(message,this, MessagePanelStyle.HISTORY);
            if(messagesContainer.getComponentCount() > 0){
                messagePanel.setBorder(BorderFactory.createMatteBorder(1,0,0,0, AppThemeColor.BORDER));
            }
            messagesContainer.add(messagePanel);
            if(this.getSize().height > SCROLL_HEIGHT) {
                this.packFrame();
                scrollPane.setPreferredSize(new Dimension(messagesContainer.getWidth(), SCROLL_HEIGHT));
                scrollPane.setSize(new Dimension(messagesContainer.getWidth(), SCROLL_HEIGHT));
                messagesContainer.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
            }else {
                scrollPane.setSize(new Dimension(messagesContainer.getWidth(), this.getSize().height));
            }
            this.packFrame();
            this.repaint();
        });
        EventRouter.INSTANCE.registerHandler(RepaintEvent.RepaintMessagePanel.class, event -> {
            this.revalidate();
            this.repaint();
        });
    }
}
