package com.mercury.platform.ui.frame.titled.chat;

import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.AddInterceptorEvent;
import com.mercury.platform.shared.events.custom.AddShowDelayEvent;
import com.mercury.platform.shared.events.custom.ChatFilterMessageEvent;
import com.mercury.platform.shared.events.custom.RemoveInterceptorEvent;
import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.ui.components.panel.chat.ChatFilterPanel;
import com.mercury.platform.ui.frame.titled.TitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.event.RepaintEvent;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class ChatFilterFrame extends TitledComponentFrame{
    private ChatFilterSettingsFrame settingsFrame;
    private ChatFilterPanel msgContainer;
    private MessageInterceptor interceptor;
    private boolean soundEnable = false;
    private boolean scrollToBottom = true;

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
        this.settingsFrame.init();
        this.msgContainer = new ChatFilterPanel(this);
        this.add(msgContainer,BorderLayout.CENTER);
        this.add(getNavigationPanel(),BorderLayout.LINE_END);
        this.pack();
    }

    private void performNewStrings(String[] strings){
        msgContainer.setNewChunks(Arrays.asList(strings));
        if (interceptor != null) {
            EventRouter.CORE.fireEvent(new RemoveInterceptorEvent(interceptor));
        }
        interceptor = new MessageInterceptor() {
            @Override
            protected void process(String message) {
                msgContainer.addMessage(message);
                if(soundEnable){
                    EventRouter.CORE.fireEvent(new ChatFilterMessageEvent());
                }
                ChatFilterFrame.this.pack();
            }

            @Override
            protected MessageFilter getFilter() {
                return message -> {
                    if(!message.contains("] $") && !message.contains("] #")){
                        return false;
                    }
                    message = StringUtils.substringAfter(message,":").toLowerCase();
                    for (String chunk : strings) {
                        chunk = chunk.toLowerCase();
                        if(message.contains(chunk)){
                            return true;
                        }
                    }
                    return false;
                };
            }
        };
        EventRouter.CORE.fireEvent(new AddInterceptorEvent(interceptor));
    }

    @Override
    protected String getFrameTitle() {
        return "Chat scanner";
    }

    @Override
    public void initHandlers() {
        EventRouter.UI.registerHandler(
                RepaintEvent.RepaintChatFilter.class,event -> {
//                    this.pack();
                    this.repaint();
                }
        );
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
                "");
        edit.addActionListener(
                action -> this.settingsFrame.showAuxiliaryFrame(
                        new Point(this.getLocation().x+this.getSize().width + 10,this.getLocation().y),
                        this.getPreferredSize().height));
        JButton clear = componentsFactory.getIconButton(
                "app/clear-history.png",
                18,
                AppThemeColor.TRANSPARENT,
                "Clear All.");
        clear.addActionListener(action -> {
            this.msgContainer.clear();
            this.pack();
            this.repaint();
        });
        JButton sound = componentsFactory.getIconButton(
                "app/sound-disable.png",
                18,
                AppThemeColor.TRANSPARENT,
                "Enable sound notification.");
        sound.addActionListener(action -> {
            if (soundEnable) {
                sound.setIcon(componentsFactory.getIcon("app/sound-disable.png", 18));
                this.soundEnable = false;
                this.repaint();
            } else {
                sound.setIcon(componentsFactory.getIcon("app/sound-enable.png", 18));
                this.soundEnable = true;
                this.repaint();
            }
        });

        JButton scrollEnd = componentsFactory.getIconButton(
                "app/scroll-end.png",
                18,
                AppThemeColor.TRANSPARENT,
                "Enable sound notification.");
        scrollEnd.addActionListener(action -> {
            if (scrollToBottom) {
                scrollEnd.setIcon(componentsFactory.getIcon("app/scroll-end-r.png", 18));
                this.scrollToBottom = false;
                this.repaint();
            } else {
                scrollEnd.setIcon(componentsFactory.getIcon("app/scroll-end.png", 18));
                this.scrollToBottom = true;
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
