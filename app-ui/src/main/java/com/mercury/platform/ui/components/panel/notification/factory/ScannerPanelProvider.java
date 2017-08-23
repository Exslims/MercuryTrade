package com.mercury.platform.ui.components.panel.notification.factory;

import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.shared.entity.message.PlainMessageDescriptor;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;
import com.mercury.platform.ui.components.panel.notification.ScannerNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.controller.NotificationScannerController;
import com.mercury.platform.ui.components.panel.notification.controller.ScannerPanelController;

public class ScannerPanelProvider extends NotificationPanelProvider<PlainMessageDescriptor, ScannerPanelController> {
    @Override
    public boolean isSuitable(NotificationType type) {
        return type.equals(NotificationType.SCANNER_MESSAGE);
    }

    @Override
    protected NotificationPanel<PlainMessageDescriptor, ScannerPanelController> getPanel() {
        ScannerNotificationPanel panel = new ScannerNotificationPanel();
        panel.setController(new NotificationScannerController(this.data));
        return panel;
    }
}
