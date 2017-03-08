package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.HistoryManager;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.shared.events.custom.NewWhispersEvent;
import com.mercury.platform.shared.events.custom.RepaintEvent;
import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.MessagePanel;
import com.mercury.platform.ui.components.panel.ScrollContainer;
import com.mercury.platform.ui.components.panel.misc.MessagePanelStyle;
import com.mercury.platform.ui.frame.TitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class HistoryFrame extends TitledComponentFrame implements MessagesContainer{
    private JPanel mainContainer;
    public HistoryFrame() {
        super("MercuryTrade");
        FrameSettings frameSettings = configManager.getFrameSettings(this.getClass().getSimpleName());
        this.setPreferredSize(frameSettings.getFrameSize());
    }

    @Override
    protected void initialize() {
        super.initialize();
        mainContainer = new ScrollContainer();
        mainContainer.setBackground(AppThemeColor.TRANSPARENT);
        mainContainer.setLayout(new BoxLayout(mainContainer,BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(mainContainer);
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
        vBar.setBackground(AppThemeColor.SLIDE_BG);
        vBar.setUI(new MercuryScrollBarUI());
        vBar.setPreferredSize(new Dimension(14, Integer.MAX_VALUE));
        vBar.setUnitIncrement(3);
        vBar.setBorder(BorderFactory.createEmptyBorder(1,1,1,2));
        vBar.addAdjustmentListener(e -> repaint());

        this.add(scrollPane,BorderLayout.CENTER);
        mainContainer.getParent().setBackground(AppThemeColor.TRANSPARENT);

        String[] messages = HistoryManager.INSTANCE.fetchNext(10);
        ArrayUtils.reverse(messages);
        for (String message : messages) {
            MessageParser parser = new MessageParser();
            Message parsedMessage = parser.parse(message);
            MessagePanel messagePanel;
            messagePanel = new MessagePanel(parsedMessage, this, MessagePanelStyle.HISTORY);
            messagePanel.disableTime();
            mainContainer.add(messagePanel);
        }
        this.miscPanel.setBorder(BorderFactory.createEmptyBorder(-2,0,-2,0));
        this.miscPanel.add(getClearButton(),0);
        this.pack();
        vBar.setValue(vBar.getMaximum());
        vBar.addAdjustmentListener((AdjustmentEvent e) -> {
            if (vBar.getValue() < 100) {
                String[] nextMessages = HistoryManager.INSTANCE.fetchNext(5);
                for (String message : nextMessages) {
                    MessageParser parser = new MessageParser();
                    Message parsedMessage = parser.parse(message);
                    MessagePanel messagePanel;
                    messagePanel = new MessagePanel(parsedMessage, this, MessagePanelStyle.HISTORY);
                    messagePanel.disableTime();
                    mainContainer.add(messagePanel, 0);
                    vBar.setValue(vBar.getValue() + 100);
                }
            }
        });
    }
    private JButton getClearButton(){
        JButton clearHistory =
                componentsFactory.getIconButton("app/clear-history.png",
                        12,
                        AppThemeColor.TRANSPARENT,
                        "Clear history");
        clearHistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HistoryManager.INSTANCE.clear();
                mainContainer.removeAll();
                pack();
                repaint();
            }
        });
        return clearHistory;
    }

    @Override
    protected String getFrameTitle() {
        return "History";
    }

    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(NewWhispersEvent.class, (MercuryEvent event) -> {
            Message message = ((NewWhispersEvent) event).getMessage();
            HistoryManager.INSTANCE.add(message);
            MessagePanel messagePanel = new MessagePanel(message, this, MessagePanelStyle.HISTORY);
            mainContainer.add(messagePanel);
            trimContainer();
            this.pack();

        });
        EventRouter.INSTANCE.registerHandler(RepaintEvent.RepaintMessagePanel.class, event -> {
            this.revalidate();
            this.repaint();
        });
    }
    private void trimContainer(){
        if(mainContainer.getComponentCount() > 40){
            for (int i = 0; i < 5; i++) {
                mainContainer.remove(0);
            }
            pack();
        }
    }

    @Override
    public void onExpandMessage() {

    }

    @Override
    public void onCollapseMessage() {

    }
}
