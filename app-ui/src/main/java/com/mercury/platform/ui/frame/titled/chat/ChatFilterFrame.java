package com.mercury.platform.ui.frame.titled.chat;

import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.entity.FrameSettings;
import com.mercury.platform.shared.store.MercuryStore;
import com.mercury.platform.ui.components.panel.chat.ChatFilterPanel;
import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.event.PackEvent;
import com.mercury.platform.ui.misc.event.RepaintEvent;
import com.mercury.platform.ui.misc.event.ScrollToTheEndEvent;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;
import java.util.List;

public class ChatFilterFrame extends AbstractTitledComponentFrame {
    private ChatFilterSettingsFrame settingsFrame;
    private ChatFilterPanel msgContainer;
    private MessageInterceptor interceptor;
    private boolean soundEnable = false;
    private boolean scrollToBottom = true;
    private JButton scrollEnd;

    public ChatFilterFrame() {
        super("MercuryTrade");
        FrameSettings frameSettings = configManager.getFrameSettings(this.getClass().getSimpleName());
        this.setPreferredSize(frameSettings.getFrameSize());
        this.settingsFrame = new ChatFilterSettingsFrame(this::performNewStrings);
    }

    @Override
    protected void initialize() {
        super.initialize();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setPreferredSize(getSize());
            }
        });
        this.hideButton.setIcon(componentsFactory.getIcon("app/hide.png",14));
        this.settingsFrame.init();
        this.msgContainer = new ChatFilterPanel();
        this.add(msgContainer,BorderLayout.CENTER);
        this.add(getNavigationPanel(),BorderLayout.LINE_END);
        this.pack();
    }

    private void performNewStrings(String[] strings){
        List<String> contains = new ArrayList<>();
        List<String> notContains = new ArrayList<>();

        Arrays.stream(strings).forEach(str -> {
            str = str.toLowerCase().trim();
            if(!str.isEmpty()) {
                if (str.contains("!")) {
                    notContains.add(str.replace("!", ""));
                } else {
                    contains.add(str);
                }
            }
        });

        msgContainer.setNewChunks(Arrays.asList(strings));
        if (interceptor != null) {
            MercuryStore.INSTANCE.removeInterceptorSubject.onNext(interceptor);
        }
        interceptor = new MessageInterceptor() {
            @Override
            protected void process(String message) {
                SwingUtilities.invokeLater(() -> {
                    msgContainer.addMessage(message);
                    ChatFilterFrame.this.pack();
                    if(scrollToBottom){
                        msgContainer.scrollToBottom(true);
                    }
                });
            }

            @Override
            protected MessageFilter getFilter() {
                return message -> {
                    if (!message.contains("] $") && !message.contains("] #")) {
                        return false;
                    }
                    message = StringUtils.substringAfter(message, ":").toLowerCase();
                    return notContains.stream().noneMatch(message::contains)
                            && contains.stream().anyMatch(message::contains);
                };
            }
        };
        MercuryStore.INSTANCE.addInterceptorSubject.onNext(interceptor);
    }

    @Override
    protected String getFrameTitle() {
        return "Chat scanner";
    }

    @Override
    public void initHandlers() {
        EventRouter.UI.registerHandler(
                RepaintEvent.RepaintChatFilter.class,event -> this.repaint()
        );
        EventRouter.UI.registerHandler(
                PackEvent.PackChatFilter.class, event -> this.pack());
        EventRouter.UI.registerHandler(ScrollToTheEndEvent.class, event -> {
            this.scrollToBottom = ((ScrollToTheEndEvent)event).isScrollToEnd();
            if(!this.scrollToBottom){
                scrollEnd.setIcon(componentsFactory.getIcon("app/scroll-end-r.png", 18));
                msgContainer.scrollToBottom(false);
            }
        });
    }

    private JPanel getNavigationPanel(){
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

        JPanel panel = componentsFactory.getTransparentPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        panel.setBackground(AppThemeColor.SLIDE_BG);

        JButton edit = componentsFactory.getIconButton(
                "app/edit.png",
                17,
                AppThemeColor.TRANSPARENT,
                "Chat Scanner settings");
        edit.addActionListener(
                action -> this.settingsFrame.showAuxiliaryFrame(
                        new Point(this.getLocation().x+this.getSize().width/2,this.getLocation().y),
                        this.getPreferredSize().height));
        JButton clear = componentsFactory.getIconButton(
                "app/clear-history.png",
                18,
                AppThemeColor.TRANSPARENT,
                "Clear all");
        clear.addActionListener(action -> {
            this.msgContainer.clear();
            this.pack();
            this.repaint();
        });
        JButton sound = componentsFactory.getIconButton(
                "app/sound-disable.png",
                18,
                AppThemeColor.TRANSPARENT,
                "Sound alert");
        sound.addActionListener(action -> {
            if (soundEnable) {
                sound.setIcon(componentsFactory.getIcon("app/sound-disable.png", 18));
                msgContainer.sound(false);
                this.soundEnable = false;
                this.repaint();
            } else {
                sound.setIcon(componentsFactory.getIcon("app/sound-enable.png", 18));
                msgContainer.sound(true);
                this.soundEnable = true;
                this.repaint();
            }
        });

        scrollEnd = componentsFactory.getIconButton(
                "app/scroll-end.png",
                18,
                AppThemeColor.TRANSPARENT,
                "Automatically scroll down");
        scrollEnd.addActionListener(action -> {
            if (scrollToBottom) {
                scrollEnd.setIcon(componentsFactory.getIcon("app/scroll-end-r.png", 18));
                this.scrollToBottom = false;
                msgContainer.scrollToBottom(false);
                this.repaint();
            } else {
                scrollEnd.setIcon(componentsFactory.getIcon("app/scroll-end.png", 18));
                this.scrollToBottom = true;
                msgContainer.scrollToBottom(true);
                this.repaint();
            }
        });

        panel.add(edit);
        panel.add(sound);
        panel.add(scrollEnd);
        panel.add(clear);

        root.add(panel,BorderLayout.CENTER);
        return root;
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
