package com.mercury.platform.ui.frame.movable;

import com.mercury.platform.core.ProdStarter;
import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.entity.message.FlowDirections;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;
import com.mercury.platform.ui.components.panel.notification.controller.stub.IncStubController;
import com.mercury.platform.ui.components.panel.notification.controller.stub.OutStubController;
import com.mercury.platform.ui.components.panel.notification.factory.NotificationPanelFactory;
import com.mercury.platform.ui.frame.titled.TestEngine;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class NotificationFrame extends AbstractMovableComponentFrame {
    private static int BUFFER_DEFAULT_HEIGHT = 1500;
    private List<NotificationPanel> notificationPanels;
    private PlainConfigurationService<NotificationSettingsDescriptor> config;
    private NotificationPanelFactory providersFactory;
    private NotificationPreProcessor preProcessor;
    private JPanel container;
    private JPanel expandPanel;
    private JPanel stubExpandPanel;
    private JPanel root;
    private boolean expanded;
    private FlowDirections flowDirections;
    private boolean dnd;

    private JPanel buffer;

    @Override
    protected void initialize() {
        super.initialize();
        this.processSEResize = false;
        this.notificationPanels = new ArrayList<>();
        this.config = Configuration.get().notificationConfiguration();
        this.flowDirections = this.config.get().getFlowDirections();
        this.componentsFactory.setScale(this.scaleConfig.get("notification"));
        this.stubComponentsFactory.setScale(this.scaleConfig.get("notification"));
        this.providersFactory = new NotificationPanelFactory();
        this.preProcessor = new NotificationPreProcessor();
    }

    @Override
    public void onViewInit() {
        this.getRootPane().setBorder(null);
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.container = new JPanel();
        this.container.setBackground(AppThemeColor.TRANSPARENT);
        this.container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        this.buffer = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.TRANSPARENT);
        this.buffer.setPreferredSize(new Dimension(10, BUFFER_DEFAULT_HEIGHT));
        this.setLocation(new Point(this.getLocation().x, this.getLocation().y - BUFFER_DEFAULT_HEIGHT));

        this.expandPanel = this.getExpandPanel();
        this.stubExpandPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.TRANSPARENT);
        this.stubExpandPanel.setPreferredSize(new Dimension(this.expandPanel.getPreferredSize().width, 5));
        this.root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.TRANSPARENT);
        this.root.add(this.stubExpandPanel, BorderLayout.LINE_START);
        this.root.add(this.container, BorderLayout.CENTER);
        this.add(this.buffer, BorderLayout.CENTER);
        this.add(root, BorderLayout.PAGE_END);
        this.setVisible(true);
        this.pack();
    }

    @Override
    @SuppressWarnings("all")
    public void subscribe() {
        MercuryStoreCore.dndSubject.subscribe(state -> {
            this.dnd = state;
            if (!this.dnd && this.notificationPanels.size() > 0) {
                this.setVisible(true);
            } else {
                this.setVisible(false);
            }
        });
        MercuryStoreCore.expiredNotificationSubject.subscribe(notification -> {
            List<NotificationPanel> stubList = new ArrayList<>(this.notificationPanels);
            String descriptorData = this.preProcessor.getDescriptorData(notification);
            stubList.forEach(it -> {
                if (this.preProcessor.getDescriptorData((NotificationDescriptor) it.getData()).equals(descriptorData)) {
                    MercuryStoreCore.removeNotificationSubject.onNext((NotificationDescriptor) it.getData());
                }
            });
        });
        MercuryStoreCore.newNotificationSubject.subscribe(notification -> {
            SwingUtilities.invokeLater(() -> {
                NotificationPanel notificationPanel = this.providersFactory.getProviderFor(notification.getType())
                        .setData(notification)
                        .setComponentsFactory(this.componentsFactory)
                        .build();
                if (preProcessor.isDuplicate(notification)) {
                    notificationPanel.setDuplicate(true);
                }
                if (this.preProcessor.isAllowed(notification)) {
                    MercuryStoreCore.soundSubject.onNext(SoundType.MESSAGE);
                    this.addNotification(notificationPanel);
                }
            });
        });
        MercuryStoreCore.newScannerMessageSubject.subscribe(message -> {
            SwingUtilities.invokeLater(() -> {
                NotificationPanel notificationPanel = this.providersFactory.getProviderFor(NotificationType.SCANNER_MESSAGE)
                        .setData(message)
                        .setComponentsFactory(this.componentsFactory)
                        .build();
                this.addNotification(notificationPanel);
            });
        });
        MercuryStoreCore.removeNotificationSubject.subscribe(notification -> {
            SwingUtilities.invokeLater(() -> {
                NotificationPanel notificationPanel = this.notificationPanels.stream()
                        .filter(it -> it.getData().equals(notification))
                        .findAny().orElse(null);
                if (notificationPanel != null) {
                    this.removeNotification(notificationPanel);
                }
            });
        });
        MercuryStoreCore.removeScannerNotificationSubject.subscribe(message -> {
            SwingUtilities.invokeLater(() -> {
                NotificationPanel notificationPanel = this.notificationPanels.stream()
                        .filter(it -> it.getData().equals(message))
                        .findAny().orElse(null);
                this.removeNotification(notificationPanel);
            });
        });
        MercuryStoreCore.hotKeySubject.subscribe(hotkeyDescriptor -> {
            SwingUtilities.invokeLater(() -> {
                if (this.notificationPanels.size() > 0 && ProdStarter.APP_STATUS.equals(FrameVisibleState.SHOW)) {
                    this.notificationPanels.get(0).onHotKeyPressed(hotkeyDescriptor);
                }
            });
        });
        MercuryStoreUI.settingsPostSubject.subscribe(state -> {
            this.validateContainer();
        });
    }

    @SuppressWarnings("all")
    private void validateContainer() {
        List<NotificationPanel> currentPanels = new ArrayList<>(this.notificationPanels);
        currentPanels.forEach(this::removeNotification);
        this.flowDirections = this.config.get().getFlowDirections();
        currentPanels.forEach(it -> {
            NotificationPanel notificationPanel = null;
            if (it.getData() instanceof NotificationDescriptor) {
                notificationPanel = this.providersFactory.getProviderFor(((NotificationDescriptor) it.getData()).getType())
                        .setData(it.getData())
                        .setComponentsFactory(this.componentsFactory)
                        .build();
            } else {
                notificationPanel = this.providersFactory.getProviderFor(NotificationType.SCANNER_MESSAGE)
                        .setData(it.getData())
                        .setComponentsFactory(this.componentsFactory)
                        .build();
            }
            this.addNotification(notificationPanel);
        });
    }

    private void addNotification(NotificationPanel notificationPanel) {
        this.notificationPanels.add(notificationPanel);
        if (this.flowDirections.equals(FlowDirections.UPWARDS)) {
            this.container.add(
                    this.componentsFactory.wrapToSlide(
                            notificationPanel, AppThemeColor.TRANSPARENT, 1, 1, 1, 1), 0);
        } else {
            this.container.add(this.componentsFactory.wrapToSlide(notificationPanel, AppThemeColor.TRANSPARENT, 1, 1, 1, 1));
        }
        int delta = -notificationPanel.getParent().getPreferredSize().height;


        if (this.notificationPanels.size() > this.config.get().getLimitCount()) {
            if (!this.expanded) {
                notificationPanel.getParent().setVisible(false);
                delta = 0;
            }
            this.root.remove(this.stubExpandPanel);
            this.root.add(this.expandPanel, BorderLayout.LINE_START);
        }
        if (this.flowDirections.equals(FlowDirections.UPWARDS) &&
                this.notificationPanels.size() > 1) {
            this.pack();
            this.changeBufferSize(delta);
        }

        if (this.flowDirections.equals(FlowDirections.UPWARDS) &&
                (notificationPanel.getAdditionalHeightDelta() > 0) && this.notificationPanels.size() == 1) {
            this.pack();
            this.changeBufferSize(-notificationPanel.getAdditionalHeightDelta());
        }
        this.pack();
        this.repaint();
    }

    private void removeNotification(NotificationPanel notificationPanel) {
        int delta = notificationPanel.getParent().getPreferredSize().height;
        notificationPanel.onViewDestroy();
        int limitCount = this.config.get().getLimitCount();
        if (!this.expanded && this.notificationPanels.size() > limitCount) {
            NotificationPanel panel = this.notificationPanels.get(limitCount);
            panel.getParent().setVisible(true);
            delta -= panel.getParent().getPreferredSize().height;
        }
        this.container.remove(notificationPanel.getParent());
        this.notificationPanels.remove(notificationPanel);
        if (this.flowDirections.equals(FlowDirections.UPWARDS)
                && this.notificationPanels.size() > 0) {
            this.changeBufferSize(delta);
        }
        if (this.notificationPanels.size() - 1 < this.config.get().getLimitCount()) {
            this.root.remove(this.expandPanel);
            this.root.add(this.stubExpandPanel, BorderLayout.LINE_START);
        }
        if (this.notificationPanels.size() == 0) {
            this.buffer.setPreferredSize(new Dimension(10, BUFFER_DEFAULT_HEIGHT));
        }
        this.pack();
        this.repaint();
    }

    @Override
    protected void onScaleLock() {
        JPanel var = this.expandPanel;
        this.expandPanel = this.getExpandPanel();
        this.stubExpandPanel.setPreferredSize(this.expandPanel.getPreferredSize());
        if (this.notificationPanels.size() > this.config.get().getLimitCount()) {
            this.remove(var);
            this.add(this.expandPanel, BorderLayout.LINE_START);
        }
        if (this.getLocation().y > 0) {
            this.setLocation(new Point(this.getLocation().x, this.getLocation().y - BUFFER_DEFAULT_HEIGHT));
        }
        super.onScaleLock();
    }

    @Override
    protected void onScaleUnlock() {
        this.setLocation(this.framesConfig.get(this.getClass().getSimpleName()).getFrameLocation());
        super.onScaleUnlock();
    }

    public void changeBufferSize(int delta) {
        if (this.flowDirections.equals(FlowDirections.UPWARDS)) {
            this.buffer.setPreferredSize(new Dimension(10, this.buffer.getPreferredSize().height + delta));
        }
    }

    @Override
    protected JPanel getPanelForPINSettings() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        JPanel panel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        panel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, AppThemeColor.BORDER));
        JLabel textLabel = this.componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 22f, "Notification panel");
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(textLabel, BorderLayout.CENTER);

        JPanel limitPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        limitPanel.setPreferredSize(this.expandPanel.getPreferredSize());
        JLabel expandIconLabel = componentsFactory.getIconLabel("app/expand_button_pin.png", 24, SwingConstants.CENTER);
        limitPanel.add(expandIconLabel, BorderLayout.CENTER);
        root.add(panel, BorderLayout.CENTER);
        root.add(limitPanel, BorderLayout.LINE_START);
        root.setPreferredSize(new Dimension((int) (400 * componentsFactory.getScale()), (int) (92 * componentsFactory.getScale())));
        return root;
    }

    @Override
    protected void onLock() {
        super.onLock();
        if (this.getLocation().y > 0) {
            this.setLocation(new Point(this.getLocation().x, this.getLocation().y - BUFFER_DEFAULT_HEIGHT));
        }
    }

    @Override
    protected void registerDirectScaleHandler() {
        MercuryStoreUI.notificationScaleSubject.subscribe(this::changeScale);
    }

    @Override
    protected void performScaling(Map<String, Float> scaleData) {
        this.componentsFactory.setScale(scaleData.get("notification"));
        this.validateContainer();
        this.pack();
        this.repaint();
    }

    @Override
    @SuppressWarnings("all")
    protected JPanel defaultView(ComponentsFactory factory) {
        TestEngine testEngine = new TestEngine();
        JPanel root = factory.getJPanel(new BorderLayout());
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        root.add(this.providersFactory
                .getProviderFor(NotificationType.INC_ITEM_MESSAGE)
                .setData(testEngine.getRandomItemIncMessage())
                .setComponentsFactory(factory)
                .setController(new IncStubController())
                .build());
        root.add(this.providersFactory
                .getProviderFor(NotificationType.OUT_ITEM_MESSAGE)
                .setData(testEngine.getRandomItemOutMessage())
                .setComponentsFactory(factory)
                .setController(new OutStubController())
                .build());
        return root;
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    private JPanel getExpandPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(AppThemeColor.MSG_HEADER);
        root.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, AppThemeColor.FRAME),
                BorderFactory.createMatteBorder(1, 1, 1, 1, AppThemeColor.MSG_HEADER_BORDER)));
        String iconPath = "app/collapse-all.png";
        JButton expandButton = componentsFactory.getIconButton(iconPath, 22, AppThemeColor.MSG_HEADER, "");
        expandButton.addActionListener(action -> {
            if (this.expanded) {
                expandButton.setIcon(this.componentsFactory.getIcon("app/collapse-all.png", 22));
                this.notificationPanels
                        .stream()
                        .skip(this.config.get().getLimitCount())
                        .forEach(it -> it.getParent().setVisible(false));
                if (this.flowDirections.equals(FlowDirections.UPWARDS)) {
                    this.changeBufferSize(this.notificationPanels
                            .stream()
                            .skip(this.config.get().getLimitCount())
                            .mapToInt(it -> it.getParent().getPreferredSize().height)
                            .sum());
                }
            } else {
                expandButton.setIcon(this.componentsFactory.getIcon("app/expand-all.png", 22));
                int delta = 0;
                for (NotificationPanel it : this.notificationPanels) {
                    if (!it.getParent().isVisible()) {
                        it.getParent().setVisible(true);
                        delta += it.getParent().getPreferredSize().height;
                    }
                }
                if (this.flowDirections.equals(FlowDirections.UPWARDS)) {
                    this.changeBufferSize(-delta);
                }
            }
            this.expanded = !this.expanded;
            this.pack();
            this.repaint();
        });
        expandButton.setAlignmentY(SwingConstants.CENTER);
        root.add(expandButton, BorderLayout.CENTER);
        return this.componentsFactory.wrapToSlide(root, AppThemeColor.TRANSPARENT, 1, 1, 1, 1);
    }
}
