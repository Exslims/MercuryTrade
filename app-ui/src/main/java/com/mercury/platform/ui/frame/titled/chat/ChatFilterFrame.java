package com.mercury.platform.ui.frame.titled.chat;

import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.config.descriptor.ScannerDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.chat.ChatFilterPanel;
import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;
import java.util.List;

public class ChatFilterFrame extends AbstractTitledComponentFrame {
    private ChatFilterSettingsFrame settingsFrame;
    private PlainConfigurationService<ScannerDescriptor> scannerService;
    private ChatFilterPanel msgContainer;
    private MessageInterceptor interceptor;
    private JPanel toolbar;
    private boolean soundEnable = false;
    private boolean scrollToBottom = true;
    private boolean chatEnable = false;
    private JButton scrollEnd;

    public ChatFilterFrame() {
        super();
        FrameDescriptor frameDescriptor = this.framesConfig.get(this.getClass().getSimpleName());
        this.setPreferredSize(frameDescriptor.getFrameSize());
        this.settingsFrame = new ChatFilterSettingsFrame(strings -> {
            if(chatEnable){
                performNewStrings(strings);
            }
        });
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.scannerService = Configuration.get().scannerConfiguration();
    }

    @Override
    public void onViewInit() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setPreferredSize(getSize());
            }
        });
        this.hideButton.setIcon(componentsFactory.getIcon("app/hide.png",14));
        this.hideButton.setBorder(BorderFactory.createEmptyBorder(0,2,0,2));
        this.headerPanel.add(getMenuButton(),BorderLayout.LINE_START);
        this.miscPanel.add(getEnableButton(),0);
//        this.miscPanel.add(getMinimizeButton(),1);
        this.miscPanel.setBorder(BorderFactory.createEmptyBorder(-4,0,0,0));
        this.settingsFrame.init();
        this.msgContainer = new ChatFilterPanel();
        this.toolbar = getToolbar();
        toolbar.setVisible(false);
        this.add(toolbar,BorderLayout.LINE_START);
        this.add(msgContainer,BorderLayout.CENTER);
        this.pack();
    }

    private JButton getEnableButton() {
        JButton enableButton = componentsFactory.getBorderedButton("Start");
        enableButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1),
                BorderFactory.createMatteBorder(1,5,1,5,AppThemeColor.TRANSPARENT)
        ));
        enableButton.setFont(componentsFactory.getFont(FontStyle.BOLD,16f));
        enableButton.setPreferredSize(new Dimension(90,23));
        enableButton.setBackground(AppThemeColor.TRANSPARENT);
        enableButton.setForeground(AppThemeColor.TEXT_SUCCESS);

        componentsFactory.setUpToggleCallbacks(enableButton,
                () -> {
                    chatEnable = false;
                    if (interceptor != null) {
                        MercuryStoreCore.removeInterceptorSubject.onNext(interceptor);
                    }
                    enableButton.setText("Start");
                    enableButton.setForeground(AppThemeColor.TEXT_SUCCESS);
                    repaint();
                },
                () -> {
                    chatEnable = true;
                    String chunkStr = StringUtils.deleteWhitespace(this.scannerService.get().getWords());
                    String[] split = chunkStr.split(",");
                    performNewStrings(split);
                    enableButton.setText("Stop");
                    enableButton.setForeground(AppThemeColor.TEXT_IMPORTANT);
                    repaint();
                }, false);
        return enableButton;
    }
    private JButton getMinimizeButton() {
        JButton minimizer = componentsFactory.getIconButton("app/minimize.png", 14, AppThemeColor.FRAME_ALPHA, "");
        minimizer.addActionListener(action -> {
            //todo
        });
        return minimizer;
    }
    private JButton getMenuButton() {
        JButton menu = componentsFactory.getIconButton("app/menu.png", 18, AppThemeColor.FRAME_ALPHA, "Menu");
        menu.setBorder(BorderFactory.createEmptyBorder(4,6,4,4));
        componentsFactory.setUpToggleCallbacks(menu,
                () -> {
                    this.toolbar.setVisible(true);
                    repaint();
                },
                () -> {
                    this.toolbar.setVisible(false);
                    repaint();
                }, true);
        return menu;
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
            MercuryStoreCore.removeInterceptorSubject.onNext(interceptor);
        }
        interceptor = new MessageInterceptor() {
            @Override
            protected void process(String message) {
                msgContainer.addMessage(message);
                ChatFilterFrame.this.pack();
                if(scrollToBottom){
                    msgContainer.scrollToBottom(true);
                }
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
        MercuryStoreCore.addInterceptorSubject.onNext(interceptor);
    }

    @Override
    protected String getFrameTitle() {
        return "Mercury: Chat scanner";
    }

    @Override
    public void subscribe() {
        MercuryStoreUI.scrollToEndSubject.subscribe(value -> {
            this.scrollToBottom = value;
            if (!this.scrollToBottom) {
                this.scrollEnd.setIcon(componentsFactory.getIcon("app/scroll-end-r.png", 18));
                this.msgContainer.scrollToBottom(false);
            }
        });
    }

    private JPanel getToolbar(){
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
                        new Point(this.getLocation().x,this.getLocation().y),
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

        this.scrollEnd = componentsFactory.getIconButton(
                "app/scroll-end.png",
                18,
                AppThemeColor.TRANSPARENT,
                "Automatically scroll down");
        this.scrollEnd.addActionListener(action -> {
            if (scrollToBottom) {
                this.scrollEnd.setIcon(componentsFactory.getIcon("app/scroll-end-r.png", 18));
                this.scrollToBottom = false;
                this.msgContainer.scrollToBottom(false);
                this.repaint();
            } else {
                this.scrollEnd.setIcon(componentsFactory.getIcon("app/scroll-end.png", 18));
                this.scrollToBottom = true;
                this.msgContainer.scrollToBottom(true);
                this.repaint();
            }
        });

        panel.add(edit);
        panel.add(sound);
        panel.add(this.scrollEnd);
        panel.add(clear);

        root.add(panel,BorderLayout.CENTER);
        return root;
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
