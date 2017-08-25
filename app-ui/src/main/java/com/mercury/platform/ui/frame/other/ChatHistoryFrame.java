package com.mercury.platform.ui.frame.other;


import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChatHistoryFrame extends AbstractOverlaidFrame {
    private VerticalScrollContainer chatContainer;
    private Timer showTimer;
    private Timer hideTimer;

    public ChatHistoryFrame() {
        super();
    }

    @Override
    protected void initialize() {
        this.componentsFactory.setScale(this.scaleConfig.get("notification"));
    }

    @Override
    public void onViewInit() {
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setOpacity(this.applicationConfig.get().getMaxOpacity() / 100f);
        this.setPreferredSize(new Dimension((int) (300 * this.componentsFactory.getScale()), (int) (150 * this.componentsFactory.getScale())));
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        root.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 2)));
        root.add(this.getChatPanel(), BorderLayout.CENTER);
        this.add(root, BorderLayout.CENTER);
        this.pack();
    }

    private JComponent getChatPanel() {
        this.chatContainer = new VerticalScrollContainer();
        this.chatContainer.setBackground(AppThemeColor.FRAME);
        this.chatContainer.setLayout(new BoxLayout(this.chatContainer, BoxLayout.Y_AXIS));

        this.chatContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hideTimer.stop();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hideChat();
            }
        });

        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(this.chatContainer);
        return verticalContainer;
    }

    @Override
    public void subscribe() {
        MercuryStoreUI.showChatHistorySubject.subscribe(definition -> {
            if (this.showTimer != null) {
                this.showTimer.stop();
            }
            if (this.hideTimer != null) {
                this.hideTimer.stop();
            }
            this.showTimer = new Timer(300, action -> {
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                if (definition.getLocation().y + this.getPreferredSize().height > dim.height) {
                    this.setLocation(definition.getLocation().x + 4, definition.getLocation().y - this.getPreferredSize().height);
                } else {
                    this.setLocation(new Point(definition.getLocation().x + 4, definition.getLocation().y));
                }
                this.chatContainer.removeAll();
                this.componentsFactory.setScale(this.scaleConfig.get("notification"));
                this.setPreferredSize(new Dimension((int) (300 * this.componentsFactory.getScale()), (int) (150 * this.componentsFactory.getScale())));
                definition.getMessages().forEach(it -> {
                    this.chatContainer.add(this.componentsFactory.getTextLabel((it.isIncoming() ? "> " : "") + it.getMessage()));
                });
                this.pack();
                this.setVisible(true);
            });
            this.showTimer.setRepeats(false);
            this.showTimer.start();
        });
        MercuryStoreUI.hideChatHistorySubject.subscribe(state -> {
            this.hideChat();
        });
    }

    private void hideChat() {
        this.showTimer.stop();
        this.hideTimer = new Timer(500, action -> {
            this.setVisible(false);
        });
        this.hideTimer.setRepeats(false);
        this.hideTimer.start();
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }


}
