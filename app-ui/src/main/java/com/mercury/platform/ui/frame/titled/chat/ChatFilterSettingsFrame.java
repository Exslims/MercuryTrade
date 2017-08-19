package com.mercury.platform.ui.frame.titled.chat;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.ScannerDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.chat.ChatMessagePanel;
import com.mercury.platform.ui.components.panel.chat.HtmlMessageBuilder;
import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatFilterSettingsFrame extends AbstractTitledComponentFrame {
    private PlainConfigurationService<ScannerDescriptor> scannerService;
    private JTextField quickResponseField;
    private MessageInterceptor currentInterceptor;
    private Map<String,String> expiresMessages;
    private HtmlMessageBuilder messageBuilder;

    public ChatFilterSettingsFrame() {
        super();
        this.processingHideEvent = false;
        this.setFocusableWindowState(true);
        this.setFocusable(true);
        this.setAlwaysOnTop(false);
    }

    @Override
    public void onViewInit() {
        this.scannerService = Configuration.get().scannerConfiguration();
        this.expiresMessages = ExpiringMap.builder()
                .expiration(10, TimeUnit.SECONDS)
                .build();
        this.messageBuilder = new HtmlMessageBuilder();
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel setupArea = componentsFactory.getTransparentPanel(new BorderLayout());
        setupArea.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));

        JLabel title = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                15f,
                "Show messages containing the following words:");
        title.setBorder(BorderFactory.createEmptyBorder(2,0,6,0));
        JTextArea words = componentsFactory.getSimpleTextArea(this.scannerService.get().getWords());
        words.setEditable(true);
        words.setCaretColor(AppThemeColor.TEXT_DEFAULT);
        words.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        words.setBackground(AppThemeColor.SLIDE_BG);

        JPanel navBar = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        Dimension buttonSize = new Dimension(90, 24);
        JButton save = componentsFactory.getBorderedButton("Save");
        save.addActionListener(action -> {
            this.scannerService.get().setWords(words.getText());
            this.scannerService.get().setResponseMessage(quickResponseField.getText());
            MercuryStoreCore.saveConfigSubject.onNext(true);

            String chunkStr = StringUtils.deleteWhitespace(words.getText());
            String[] split = chunkStr.split(",");
            this.performNewStrings(split);
            this.hideComponent();
        });
        JButton cancel = componentsFactory.getBorderedButton("Cancel");
        cancel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 3)
        ));
        cancel.setBackground(AppThemeColor.FRAME);
        cancel.addActionListener(action -> {
            hideComponent();
        });
        save.setPreferredSize(buttonSize);
        cancel.setPreferredSize(buttonSize);
        navBar.add(cancel);
        navBar.add(save);

        setupArea.add(title,BorderLayout.PAGE_START);
        setupArea.add(words,BorderLayout.CENTER);

        root.add(setupArea,BorderLayout.CENTER);
        root.add(getMemo(),BorderLayout.LINE_END);

        JPanel padding = componentsFactory.getTransparentPanel(new BorderLayout());
        padding.setBorder(BorderFactory.createEmptyBorder(0,4,4,4));
        padding.add(getResponsePanel(),BorderLayout.CENTER);
        root.add(padding,BorderLayout.PAGE_END);
        this.add(root,BorderLayout.CENTER);
        this.add(navBar,BorderLayout.PAGE_END);
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
        if (this.currentInterceptor != null) {
            MercuryStoreCore.removeInterceptorSubject.onNext(this.currentInterceptor);
        }
        this.currentInterceptor = new MessageInterceptor() {
            @Override
            protected void process(String stubMessage) {
                String message = StringUtils.substringAfter(stubMessage,"] $");
                if(message.isEmpty()){
                    message = StringUtils.substringAfter(stubMessage,"] #");
                }
                if(!message.isEmpty()){
                    String nickname = StringUtils.substringBefore(message, ":");
                    String messageContent = StringUtils.substringAfter(message, nickname + ":");
                    nickname = StringUtils.deleteWhitespace(nickname);
                    if(nickname.contains(">")) {
                        nickname = StringUtils.substringAfterLast(nickname, ">");
                    }
                    if(!expiresMessages.containsValue(message)){
                        NotificationDescriptor notificationDescriptor = new NotificationDescriptor();
                        notificationDescriptor.setType(NotificationType.SCANNER_MESSAGE);
                        notificationDescriptor.setSourceString(messageBuilder.build(messageContent));
                        notificationDescriptor.setWhisperNickname(nickname);
                        MercuryStoreCore.newNotificationSubject.onNext(notificationDescriptor);
                        expiresMessages.put(nickname,message);
                        MercuryStoreCore.soundSubject.onNext(SoundType.CHAT_SCANNER);
                    }
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
        MercuryStoreCore.addInterceptorSubject.onNext(this.currentInterceptor);
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.setPreferredSize(new Dimension(350,300));
    }
    private JPanel getResponsePanel(){
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel setupArea = componentsFactory.getTransparentPanel(new BorderLayout());
        setupArea.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));

        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        root.setBackground(AppThemeColor.SLIDE_BG);

        JButton quickResponse = componentsFactory.getIconButton("app/chat_scanner_response.png", 18f, AppThemeColor.SLIDE_BG, "Quick response");
        this.quickResponseField = componentsFactory.getTextField(this.scannerService.get().getResponseMessage(),FontStyle.REGULAR,16f);
        this.quickResponseField.setBackground(AppThemeColor.SLIDE_BG);
        this.quickResponseField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.HEADER,1),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,3)
        ));
        setupArea.add(quickResponse,BorderLayout.LINE_START);
        setupArea.add(this.quickResponseField,BorderLayout.CENTER);
        root.add(setupArea,BorderLayout.CENTER);
        return root;
    }

    private JPanel getMemo(){
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        JLabel title = componentsFactory.getTextLabel(
                "Memo:",
                FontStyle.REGULAR);
        title.setBorder(BorderFactory.createEmptyBorder(6,0,2,0));


        JPanel itemsPanel = componentsFactory.getTransparentPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel,BoxLayout.Y_AXIS));

        itemsPanel.add(componentsFactory.getTextLabel("not case sensitive",FontStyle.REGULAR,17));
        itemsPanel.add(componentsFactory.getTextLabel("! - NOT (!wtb,!wts)",FontStyle.REGULAR,17));
        itemsPanel.add(componentsFactory.getTextLabel(", - separator",FontStyle.REGULAR,17));
        root.add(title,BorderLayout.PAGE_START);
        root.add(itemsPanel,BorderLayout.CENTER);
        return root;
    }

    @Override
    protected String getFrameTitle() {
        return "Chat filter settings";
    }

    @Override
    public void subscribe() {

    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
