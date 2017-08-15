package com.mercury.platform.ui.frame.titled.container;

import com.mercury.platform.shared.HistoryManager;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.notification.InMessagePanel;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.components.panel.notification.NotificationMessageController;
import com.mercury.platform.ui.components.panel.notification.MessagePanelStyle;
import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class HistoryFrame extends AbstractTitledComponentFrame implements HistoryContainer {
    private JPanel mainContainer;
    public HistoryFrame() {
        super();
        FrameDescriptor frameDescriptor = this.framesConfig.get(this.getClass().getSimpleName());
        this.setPreferredSize(frameDescriptor.getFrameSize());
        this.componentsFactory.setScale(this.scaleConfig.get("other"));
    }
    @Override
    public void onViewInit() {
        this.mainContainer = new VerticalScrollContainer();
        this.mainContainer.setBackground(AppThemeColor.TRANSPARENT);
        this.mainContainer.setLayout(new BoxLayout(this.mainContainer,BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(this.mainContainer);
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
            NotificationDescriptor parsedNotificationDescriptor = parser.parse(message);
            if(parsedNotificationDescriptor != null) {
                InMessagePanel inMessagePanel = new InMessagePanel(
                        parsedNotificationDescriptor,
                        MessagePanelStyle.HISTORY,
                        new NotificationMessageController(parsedNotificationDescriptor),
                        this.componentsFactory);
                inMessagePanel.disableTime();
                mainContainer.add(inMessagePanel);
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
                    NotificationDescriptor parsedNotificationDescriptor = parser.parse(message);
                    if(parsedNotificationDescriptor != null) {
                        InMessagePanel inMessagePanel = new InMessagePanel(
                                parsedNotificationDescriptor,
                                MessagePanelStyle.HISTORY,
                                new NotificationMessageController(parsedNotificationDescriptor),
                                this.componentsFactory);
                        inMessagePanel.disableTime();
                        this.mainContainer.add(inMessagePanel, 0);
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
        return "Mercury: History";
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.messageSubject.subscribe(message -> SwingUtilities.invokeLater(()-> {
            HistoryManager.INSTANCE.add(message);
            InMessagePanel inMessagePanel = new InMessagePanel(
                    message,
                    MessagePanelStyle.HISTORY,
                    new NotificationMessageController(message),
                    this.componentsFactory);
            this.mainContainer.add(inMessagePanel);
            this.trimContainer();
            this.pack();
        }));
        MercuryStoreUI.reloadMessageSubject.subscribe(this::onReloadMessage);
    }
    private void trimContainer(){
        if(mainContainer.getComponentCount() > 40){
            for (int i = 0; i < 5; i++) {
                mainContainer.remove(0);
            }
            this.pack();
        }
    }

    @Override
    public void onReloadMessage(InMessagePanel inMessagePanel) {
        inMessagePanel.setStyle(MessagePanelStyle.RELOADED);
        inMessagePanel.setPreferredSize(new Dimension(this.getWidth()-10, inMessagePanel.getPreferredSize().height));
        this.pack();
        this.repaint();
    }
}
