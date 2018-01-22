package com.mercury.platform.ui.frame.titled;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.core.utils.interceptor.filter.MessageMatcher;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.config.descriptor.ScannerDescriptor;
import com.mercury.platform.shared.entity.message.PlainMessageDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.chat.HtmlMessageBuilder;
import com.mercury.platform.ui.misc.AppThemeColor;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatScannerFrame extends AbstractTitledComponentFrame {
    private PlainConfigurationService<ScannerDescriptor> scannerService;
    private PlainConfigurationService<NotificationSettingsDescriptor> notificationConfig;
    private MessageInterceptor currentInterceptor;
    private Map<String, String> expiresMessages;
    private HtmlMessageBuilder messageBuilder;
    private boolean running;

    public ChatScannerFrame() {
        super();
        this.processingHideEvent = false;
        this.setFocusableWindowState(true);
        this.setFocusable(true);
        this.setAlwaysOnTop(false);
    }

    @Override
    public void onViewInit() {
        this.scannerService = Configuration.get().scannerConfiguration();
        this.notificationConfig = Configuration.get().notificationConfiguration();
        this.expiresMessages = ExpiringMap.builder()
                .expiration(10, TimeUnit.SECONDS)
                .build();
        this.messageBuilder = new HtmlMessageBuilder();
        this.initHeaderBar();
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel setupArea = componentsFactory.getTransparentPanel(new BorderLayout());
        setupArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JLabel title = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                15f,
                "Show messages containing the following words:");
        title.setBorder(BorderFactory.createEmptyBorder(2, 0, 6, 0));
        JTextArea words = componentsFactory.getSimpleTextArea(this.scannerService.get().getWords());
        words.setEditable(true);
        words.setCaretColor(AppThemeColor.TEXT_DEFAULT);
        words.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        words.setBackground(AppThemeColor.SLIDE_BG);

        JPanel navBar = componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER), AppThemeColor.FRAME);
        Dimension buttonSize = new Dimension(90, 24);
        JButton save = componentsFactory.getBorderedButton("Save");
        save.addActionListener(action -> {
            this.scannerService.get().setWords(words.getText());
            MercuryStoreCore.saveConfigSubject.onNext(true);

            String[] split = words.getText().split(",");
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

        setupArea.add(title, BorderLayout.PAGE_START);
        setupArea.add(words, BorderLayout.CENTER);

        root.add(setupArea, BorderLayout.CENTER);
        root.add(getMemo(), BorderLayout.LINE_END);

        JPanel propertiesPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);

        JLabel quickResponseLabel = this.componentsFactory.getIconLabel(HotKeyType.N_QUICK_RESPONSE.getIconPath(), 18);
        quickResponseLabel.setFont(this.componentsFactory.getFont(FontStyle.REGULAR, 16));
        quickResponseLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
        quickResponseLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        quickResponseLabel.setText("Response message:");
        propertiesPanel.add(quickResponseLabel, BorderLayout.LINE_START);
        JTextField quickResponseField = this.componentsFactory.getTextField(this.scannerService.get().getResponseMessage(), FontStyle.BOLD, 15f);
        quickResponseField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                scannerService.get().setResponseMessage(quickResponseField.getText());
            }
        });
        propertiesPanel.add(this.componentsFactory.wrapToSlide(quickResponseField, AppThemeColor.FRAME, 0, 4, 0, 4), BorderLayout.CENTER);

        root.add(propertiesPanel, BorderLayout.PAGE_END);
        this.add(root, BorderLayout.CENTER);
        this.add(navBar, BorderLayout.PAGE_END);
        this.pack();
    }

    private void initHeaderBar() {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 0, 4, 0), AppThemeColor.HEADER);
        JLabel statusLabel = componentsFactory.getTextLabel(
                FontStyle.BOLD,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                16f,
                "Status: stopped");

        JButton processButton = componentsFactory.getBorderedButton("Start");
        processButton.setFont(this.componentsFactory.getFont(FontStyle.BOLD, 16f));
        processButton.setPreferredSize(new Dimension(80, 20));
        processButton.addActionListener(action -> {
            if (this.running) {
                this.running = false;
                processButton.setText("Start");
                statusLabel.setText("Status: stopped");
                if (this.currentInterceptor != null) {
                    MercuryStoreCore.removeInterceptorSubject.onNext(this.currentInterceptor);
                }
            } else {
                this.running = true;
                processButton.setText("Stop");
                statusLabel.setText("Status: running");
                this.performNewStrings(this.scannerService.get().getWords().split(","));
            }
        });
        root.add(statusLabel);
        root.add(processButton);
        this.miscPanel.add(root, BorderLayout.CENTER);
    }

    private void performNewStrings(String[] strings) {
        if (this.running) {
            List<String> contains = new ArrayList<>();
            List<String> notContains = new ArrayList<>();

            Arrays.stream(strings).forEach(str -> {
                str = str.toLowerCase().trim();
                if (!str.isEmpty()) {
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
                    messageBuilder.setChunkStrings(Arrays.asList(strings));
                    String message = StringUtils.substringAfter(stubMessage, "] $");
                    if (message.isEmpty()) {
                        message = StringUtils.substringAfter(stubMessage, "] #");
                    }
                    if (!message.isEmpty()) {
                        Pattern pattern = Pattern.compile("^(\\<.+?\\>)?\\s?(.+?):(.+)$");
                        Matcher matcher = pattern.matcher(message);
                        if (matcher.find() && !expiresMessages.containsValue(message)) {
                            PlainMessageDescriptor descriptor = new PlainMessageDescriptor();
                            descriptor.setNickName(matcher.group(2));
                            descriptor.setMessage(messageBuilder.build(matcher.group(3)));

                            expiresMessages.put(descriptor.getNickName(), message);
                            if (notificationConfig.get().isScannerNotificationEnable()) {
                                MercuryStoreCore.newScannerMessageSubject.onNext(descriptor);
                            }
                            MercuryStoreCore.soundSubject.onNext(SoundType.CHAT_SCANNER);
                        }
                    }
                }

                @Override
                protected MessageMatcher match() {
                    return message -> {
                        if (!message.contains("] $") && !message.contains("] #")) {
                            return false;
                        }
                        final String separator = message.contains("] $") ? "] $" : "] #";
                        message = StringUtils.substringAfter(message, separator).toLowerCase();
                        message = StringUtils.substringAfter(message, ": ").toLowerCase();
                        return notContains.stream().noneMatch(message::contains)
                                && contains.stream().anyMatch(message::contains);
                    };
                }
            };
            MercuryStoreCore.addInterceptorSubject.onNext(this.currentInterceptor);
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.setPreferredSize(new Dimension(350, 300));
    }

    private JPanel getMemo() {
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        JLabel title = componentsFactory.getTextLabel(
                "Memo:",
                FontStyle.REGULAR);
        title.setBorder(BorderFactory.createEmptyBorder(6, 0, 2, 0));


        JPanel itemsPanel = componentsFactory.getTransparentPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));

        itemsPanel.add(componentsFactory.getTextLabel("not case sensitive", FontStyle.REGULAR, 17));
        itemsPanel.add(componentsFactory.getTextLabel("! - NOT (!wtb,!wts)", FontStyle.REGULAR, 17));
        itemsPanel.add(componentsFactory.getTextLabel(", - separator", FontStyle.REGULAR, 17));
        root.add(title, BorderLayout.PAGE_START);
        root.add(itemsPanel, BorderLayout.CENTER);
        return root;
    }

    @Override
    protected String getFrameTitle() {
        return "Chat scanner";
    }

    @Override
    public void subscribe() {

    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
