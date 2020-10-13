package com.mercury.platform.ui.frame.titled;

import com.mercury.platform.shared.HistoryManager;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.datatable.MColumn;
import com.mercury.platform.ui.components.datatable.MDataTable;
import com.mercury.platform.ui.components.datatable.data.DataRequest;
import com.mercury.platform.ui.components.datatable.data.MDataService;
import com.mercury.platform.ui.components.datatable.renderer.NotificationTypeRenderer;
import com.mercury.platform.ui.components.datatable.renderer.PlainIconRenderer;
import com.mercury.platform.ui.components.datatable.renderer.PlainTextRenderer;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;
import com.mercury.platform.ui.components.panel.notification.factory.NotificationPanelFactory;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HistoryFrame extends AbstractTitledComponentFrame {
    private JPanel mainContainer;
    private NotificationPanelFactory factory;
    private List<NotificationDescriptor> currentMessages;
    private MDataTable<NotificationDescriptor> dataTable;

    public HistoryFrame() {
        super();
        FrameDescriptor frameDescriptor = this.framesConfig.get(this.getClass().getSimpleName());
        this.setPreferredSize(frameDescriptor.getFrameSize());
        this.componentsFactory.setScale(this.scaleConfig.get("other"));
    }

    @Override
    public void onViewInit() {
        //TODO: Make it work
//        initNewHistoryFrame();
        //TODO: Test this
        initHistoryFrame();
    }

    private void initNewHistoryFrame() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        MColumn[] columns = {
                new MColumn("Item name",
                            "ItemName|(CurrForSaleCount+CurrForSaleTitle)",
                            false,
                            false,
                            PlainIconRenderer.class),
                new MColumn("Type", "Type", false, false, NotificationTypeRenderer.class),
                new MColumn("Currency", "CurCount+Currency", false, false, PlainIconRenderer.class),
                new MColumn("League", "League", false, false, PlainTextRenderer.class),
                new MColumn("Nickname", "WhisperNickname", false, false, PlainTextRenderer.class),
                new MColumn("Offer", "Offer", false, false, PlainTextRenderer.class),
                new MColumn("Tab name", "TabName", false, false, PlainTextRenderer.class),
                };
        MDataService<NotificationDescriptor> dataService = new MDataService<NotificationDescriptor>() {
            @Override
            public NotificationDescriptor[] getData(DataRequest request) {
                TestEngine testEngine = new TestEngine();
                NotificationDescriptor[] notificationDescriptors = new NotificationDescriptor[15];
                Random random = new Random();
                for (int i = 0; i < 15; i++) {
                    switch (random.nextInt(4)) {
                        case 0: {
                            notificationDescriptors[i] = testEngine.getRandomItemIncMessage();
                            break;
                        }
                        case 1: {
                            notificationDescriptors[i] = testEngine.getRandomCurrencyIncMessage();
                            break;
                        }
                        case 2: {
                            notificationDescriptors[i] = testEngine.getRandomItemOutMessage();
                            break;
                        }
                        case 3: {
                            notificationDescriptors[i] = testEngine.getRandomCurrencyOutMessage();
                            break;
                        }
                    }
                }
                return notificationDescriptors;
            }

            @Override
            public void removeData(NotificationDescriptor data) {

            }
        };
        this.dataTable = new MDataTable<>(columns, dataService, 10);
        this.dataTable.addCellRenderer(NotificationType.class, new NotificationTypeRenderer());

        root.add(this.componentsFactory.wrapToSlide(this.getToolBar(), 0, 0, 2, 0), BorderLayout.PAGE_START);
        root.add(this.dataTable, BorderLayout.CENTER);

        this.add(this.componentsFactory.wrapToSlide(root), BorderLayout.CENTER);
        this.pack();
    }

    private void initHistoryFrame() {
        this.factory = new NotificationPanelFactory();
        this.currentMessages = new ArrayList<>();
        this.mainContainer = new VerticalScrollContainer();
        this.mainContainer.setBackground(AppThemeColor.FRAME);
        this.mainContainer.setLayout(new BoxLayout(this.mainContainer, BoxLayout.Y_AXIS));

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
        vBar.setPreferredSize(new Dimension(16, Integer.MAX_VALUE));
        vBar.setUnitIncrement(3);
        vBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 2));
        vBar.addAdjustmentListener(e -> repaint());

        this.add(scrollPane, BorderLayout.CENTER);
        mainContainer.getParent().setBackground(AppThemeColor.FRAME);

        String[] messages = HistoryManager.INSTANCE.fetchNext(10);
        ArrayUtils.reverse(messages);
        for (String message : messages) {
            MessageParser parser = new MessageParser();
            NotificationDescriptor parsedNotificationDescriptor = parser.parse(message);
            if (parsedNotificationDescriptor != null) {
                NotificationPanel panel = this.factory.getProviderFor(NotificationType.HISTORY)
                                                      .setData(parsedNotificationDescriptor)
                                                      .setComponentsFactory(this.componentsFactory)
                                                      .build();
                this.currentMessages.add(parsedNotificationDescriptor);
                mainContainer.add(panel);
            }
        }
        this.miscPanel.add(getClearButton(), 0);
        this.pack();
        vBar.setValue(vBar.getMaximum());
        vBar.addAdjustmentListener((AdjustmentEvent e) -> {
            if (vBar.getValue() < 100) {
                String[] nextMessages = HistoryManager.INSTANCE.fetchNext(5);
                for (String message : nextMessages) {
                    MessageParser parser = new MessageParser();
                    NotificationDescriptor parsedNotificationDescriptor = parser.parse(message);
                    if (parsedNotificationDescriptor != null) {
                        NotificationPanel panel = this.factory.getProviderFor(NotificationType.HISTORY)
                                                              .setData(parsedNotificationDescriptor)
                                                              .setComponentsFactory(this.componentsFactory)
                                                              .build();
                        this.currentMessages.add(parsedNotificationDescriptor);
                        mainContainer.add(panel, 0);
                    }
                    vBar.setValue(vBar.getValue() + 100);
                }
            }
        });
    }

    private JPanel getToolBar() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        JPanel leftToolbar = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightToolbar = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton test1 = componentsFactory.getBorderedButton("Test1", 16);
        test1.addActionListener(e -> {
            this.dataTable.reload();
        });
        test1.setPreferredSize(new Dimension(110, 26));

        JButton test2 = componentsFactory.getBorderedButton("Test2", 16);
        test2.addActionListener(e -> {
        });
        test2.setPreferredSize(new Dimension(110, 26));
        JButton test3 = componentsFactory.getBorderedButton("Test3", 16);
        test3.addActionListener(e -> {
        });
        test3.setPreferredSize(new Dimension(110, 26));

        leftToolbar.add(test1);
        rightToolbar.add(test2);
        rightToolbar.add(test3);
        root.add(leftToolbar, BorderLayout.LINE_START);
        root.add(rightToolbar, BorderLayout.LINE_END);
        return root;
    }

    private JButton getClearButton() {
        JButton clearHistory =
                componentsFactory.getIconButton("app/clear-history.png",
                                                13,
                                                AppThemeColor.HEADER,
                                                "Clear history");
        clearHistory.addActionListener(action -> {
            HistoryManager.INSTANCE.clear();
            this.mainContainer.removeAll();
            this.pack();
        });
        return clearHistory;
    }

    @Override
    protected String getFrameTitle() {
        return "Mercury: History";
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.newNotificationSubject.subscribe(message -> SwingUtilities.invokeLater(() -> {
            if (!currentMessages.contains(message)) {
                HistoryManager.INSTANCE.add(message);
                NotificationPanel panel = this.factory.getProviderFor(NotificationType.HISTORY)
                                                      .setData(message)
                                                      .setComponentsFactory(this.componentsFactory)
                                                      .build();
                mainContainer.add(panel);
                this.currentMessages.add(message);
                this.trimContainer();
                this.pack();
            }
        }));
    }

    private void trimContainer() {
        if (mainContainer.getComponentCount() > 40) {
            for (int i = 0; i < 5; i++) {
                mainContainer.remove(0);
            }
            this.pack();
        }
    }
}
