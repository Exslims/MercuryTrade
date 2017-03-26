package com.mercury.platform.ui.frame.titled;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.HistoryManager;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.shared.events.custom.NewWhispersEvent;
import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.message.MessagePanel;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.components.panel.message.NotificationMessageController;
import com.mercury.platform.ui.components.panel.message.MessagePanelStyle;
import com.mercury.platform.ui.frame.titled.container.HistoryContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.event.ReloadMessageEvent;
import com.mercury.platform.ui.misc.event.RepaintEvent;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class HistoryFrame extends TitledComponentFrame implements HistoryContainer {
    private JPanel mainContainer;
    public HistoryFrame() {
        super("MercuryTrade");
        FrameSettings frameSettings = configManager.getFrameSettings(this.getClass().getSimpleName());
        this.setPreferredSize(frameSettings.getFrameSize());
        this.componentsFactory.setScale(ConfigManager.INSTANCE.getScaleData().getNotificationScale());
    }

    @Override
    protected void initialize() {
        super.initialize();
        mainContainer = new VerticalScrollContainer();
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
            if(parsedMessage != null) {
                MessagePanel messagePanel = new MessagePanel(
                        parsedMessage,
                        MessagePanelStyle.HISTORY,
                        new NotificationMessageController(parsedMessage),
                        this.componentsFactory);
                messagePanel.disableTime();
                mainContainer.add(messagePanel);
            }
        }
        this.miscPanel.add(getClearButton(),0);
        this.pack();
        vBar.setValue(vBar.getMaximum());
        vBar.addAdjustmentListener((AdjustmentEvent e) -> {
            if (vBar.getValue() < 100) {
                String[] nextMessages = HistoryManager.INSTANCE.fetchNext(5);
                for (String message : nextMessages) {
                    MessageParser parser = new MessageParser();
                    Message parsedMessage = parser.parse(message);
                    if(parsedMessage != null) {
                        MessagePanel messagePanel = new MessagePanel(
                                parsedMessage,
                                MessagePanelStyle.HISTORY,
                                new NotificationMessageController(parsedMessage),
                                this.componentsFactory);
                        messagePanel.disableTime();
                        mainContainer.add(messagePanel, 0);
                    }
                    vBar.setValue(vBar.getValue() + 100);
                }
            }
        });
    }
    private JButton getClearButton(){
        JButton clearHistory =
                componentsFactory.getIconButton("app/clear-history.png",
                        13,
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
        clearHistory.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
        return clearHistory;
    }

    @Override
    protected String getFrameTitle() {
        return "History";
    }

    @Override
    public void initHandlers() {
        EventRouter.CORE.registerHandler(NewWhispersEvent.class, (MercuryEvent event) -> {
            SwingUtilities.invokeLater(()-> {
                Message message = ((NewWhispersEvent) event).getMessage();
                HistoryManager.INSTANCE.add(message);
                MessagePanel messagePanel = new MessagePanel(
                        message,
                        MessagePanelStyle.HISTORY,
                        new NotificationMessageController(message),
                        this.componentsFactory);
                mainContainer.add(messagePanel);
                trimContainer();
                this.pack();
            });
        });
        EventRouter.UI.registerHandler(ReloadMessageEvent.class,event -> {
            onReloadMessage(((ReloadMessageEvent)event).getPanel());
        });
        EventRouter.UI.registerHandler(RepaintEvent.RepaintMessageFrame.class, event -> {
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
    public void onReloadMessage(MessagePanel messagePanel) {
        messagePanel.setStyle(MessagePanelStyle.SP_MODE);
        messagePanel.setPreferredSize(new Dimension(this.getWidth()-10,messagePanel.getPreferredSize().height));
        this.pack();
        this.repaint();
    }
}
