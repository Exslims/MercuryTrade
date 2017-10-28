package com.mercury.platform.ui.frame.titled;

import com.mercury.platform.shared.HistoryManager;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.datatable.MColumn;
import com.mercury.platform.ui.components.datatable.MDataTable;
import com.mercury.platform.ui.components.datatable.data.DataRequest;
import com.mercury.platform.ui.components.datatable.data.MDataService;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;
import com.mercury.platform.ui.components.panel.notification.factory.NotificationPanelFactory;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class HistoryFrame extends AbstractTitledComponentFrame {
    private JPanel mainContainer;
    private NotificationPanelFactory factory;
    private List<NotificationDescriptor> currentMessages;

    public HistoryFrame() {
        super();
        FrameDescriptor frameDescriptor = this.framesConfig.get(this.getClass().getSimpleName());
        this.setPreferredSize(frameDescriptor.getFrameSize());
        this.componentsFactory.setScale(this.scaleConfig.get("other"));
    }

    @Override
    public void onViewInit() {
        MColumn[] columns = {
                new MColumn("1", null, false, false, String.class),
                new MColumn("2", null, false, false, String.class),
                new MColumn("3", null, false, false, String.class),
                new MColumn("4", null, false, false, String.class),
                new MColumn("5", null, false, false, String.class),
        };
        MDataService<NotificationDescriptor> dataService = new MDataService<NotificationDescriptor>() {
            @Override
            public NotificationDescriptor[] getData(DataRequest request) {
                return new NotificationDescriptor[0];
            }
        };
        MDataTable<NotificationDescriptor> dataTable = new MDataTable<>(columns, dataService, 10);
        this.add(this.componentsFactory.wrapToSlide(dataTable), BorderLayout.CENTER);
//        this.factory = new NotificationPanelFactory();
//        this.currentMessages = new ArrayList<>();
//        this.mainContainer = new VerticalScrollContainer();
//        this.mainContainer.setBackground(AppThemeColor.FRAME);
//        this.mainContainer.setLayout(new BoxLayout(this.mainContainer, BoxLayout.Y_AXIS));
//
//        JScrollPane scrollPane = new JScrollPane(this.mainContainer);
//        scrollPane.setBorder(null);
//        scrollPane.setBackground(AppThemeColor.FRAME);
//        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.addMouseWheelListener(new MouseAdapter() {
//            @Override
//            public void mouseWheelMoved(MouseWheelEvent e) {
//                HistoryFrame.this.repaint();
//            }
//        });
//        JScrollBar vBar = scrollPane.getVerticalScrollBar();
//        vBar.setBackground(AppThemeColor.SLIDE_BG);
//        vBar.setUI(new MercuryScrollBarUI());
//        vBar.setPreferredSize(new Dimension(16, Integer.MAX_VALUE));
//        vBar.setUnitIncrement(3);
//        vBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 2));
//        vBar.addAdjustmentListener(e -> repaint());
//
//        this.add(scrollPane, BorderLayout.CENTER);
//        mainContainer.getParent().setBackground(AppThemeColor.FRAME);
//
//        String[] messages = HistoryManager.INSTANCE.fetchNext(10);
//        ArrayUtils.reverse(messages);
//        for (String message : messages) {
//            MessageParser parser = new MessageParser();
//            NotificationDescriptor parsedNotificationDescriptor = parser.parse(message);
//            if (parsedNotificationDescriptor != null) {
//                NotificationPanel panel = this.factory.getProviderFor(NotificationType.HISTORY)
//                        .setData(parsedNotificationDescriptor)
//                        .setComponentsFactory(this.componentsFactory)
//                        .build();
//                this.currentMessages.add(parsedNotificationDescriptor);
//                mainContainer.add(panel);
//            }
//        }
//        this.miscPanel.add(getClearButton(), 0);
//        this.pack();
//        vBar.setValue(vBar.getMaximum());
//        vBar.addAdjustmentListener((AdjustmentEvent e) -> {
//            if (vBar.getValue() < 100) {
//                String[] nextMessages = HistoryManager.INSTANCE.fetchNext(5);
//                for (String message : nextMessages) {
//                    MessageParser parser = new MessageParser();
//                    NotificationDescriptor parsedNotificationDescriptor = parser.parse(message);
//                    if (parsedNotificationDescriptor != null) {
//                        NotificationPanel panel = this.factory.getProviderFor(NotificationType.HISTORY)
//                                .setData(parsedNotificationDescriptor)
//                                .setComponentsFactory(this.componentsFactory)
//                                .build();
//                        this.currentMessages.add(parsedNotificationDescriptor);
//                        mainContainer.add(panel, 0);
//                    }
//                    vBar.setValue(vBar.getValue() + 100);
//                }
//            }
//        });
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
