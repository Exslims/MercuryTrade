package com.mercury.platform.ui.misc;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.ui.adr.routing.AdrComponentDefinition;
import com.mercury.platform.ui.adr.routing.AdrPageDefinition;
import com.mercury.platform.ui.components.panel.grid.ItemInfoPanel;
import com.mercury.platform.ui.components.panel.grid.TabInfoPanel;
import com.mercury.platform.ui.dialog.DialogCallback;
import com.mercury.platform.ui.frame.other.ChatHistoryDefinition;
import com.mercury.platform.ui.manager.routing.SettingsPage;
import rx.subjects.PublishSubject;

import java.util.Map;

public class MercuryStoreUI {
    public static final PublishSubject<ItemTradeNotificationDescriptor> closeGridItemSubject = PublishSubject.create();
    public static final PublishSubject<NotificationDescriptor> closeMessage = PublishSubject.create();
    public static final PublishSubject<Boolean> collapseMessageSubject = PublishSubject.create();
    public static final PublishSubject<TabInfoPanel> dismissTabInfoPanelSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> expandMessageSubject = PublishSubject.create();
    public static final PublishSubject<ItemInfoPanel> itemCellStateSubject = PublishSubject.create();
    //Scale
    public static final PublishSubject<Map<String, Float>> saveScaleSubject = PublishSubject.create();
    public static final PublishSubject<Float> notificationScaleSubject = PublishSubject.create();
    public static final PublishSubject<Float> taskBarScaleSubject = PublishSubject.create();
    public static final PublishSubject<Float> itemPanelScaleSubject = PublishSubject.create();

    public static final PublishSubject<Boolean> scrollToEndSubject = PublishSubject.create();
    public static final PublishSubject<ItemTradeNotificationDescriptor> showItemGridSubject = PublishSubject.create();
    public static final PublishSubject<Class<?>> packSubject = PublishSubject.create();
    public static final PublishSubject<Class<?>> repaintSubject = PublishSubject.create();

    //adr
    public static final PublishSubject<AdrPageDefinition> adrStateSubject = PublishSubject.create();
    public static final PublishSubject<AdrComponentDefinition> adrComponentStateSubject = PublishSubject.create();
    public static final PublishSubject<AdrComponentDescriptor> adrRemoveComponentSubject = PublishSubject.create();
    public static final PublishSubject<AdrComponentDescriptor> adrPostOperationsComponentSubject = PublishSubject.create();
    public static final PublishSubject<String> adrSelectProfileSubject = PublishSubject.create();
    public static final PublishSubject<String> adrNewProfileSubject = PublishSubject.create();
    public static final PublishSubject<AdrProfileDescriptor> adrRemoveProfileSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> adrRenameProfileSubject = PublishSubject.create();
    public static final PublishSubject<AdrComponentDescriptor> adrSelectSubject = PublishSubject.create();
    public static final PublishSubject<AdrComponentDescriptor> adrReloadSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> adrManagerPack = PublishSubject.create();
    public static final PublishSubject<Boolean> adrManagerRepaint = PublishSubject.create();
    public static final PublishSubject<Boolean> adrRepaintSubject = PublishSubject.create();
    public static final PublishSubject<AdrComponentDescriptor> adrExportSubject = PublishSubject.create();
    public static final PublishSubject<AdrComponentDescriptor> adrUpdateSubject = PublishSubject.create();
    public static final PublishSubject<DialogCallback<String>> adrOpenIconSelectSubject = PublishSubject.create();

    public static final PublishSubject<SettingsPage> settingsStateSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> settingsRepaintSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> settingsPackSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> settingsSaveSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> settingsRestoreSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> settingsPostSubject = PublishSubject.create();

    public static final PublishSubject<Boolean> onDestroySubject = PublishSubject.create();

    public static final PublishSubject<ChatHistoryDefinition> showChatHistorySubject = PublishSubject.create();
    public static final PublishSubject<Boolean> hideChatHistorySubject = PublishSubject.create();
}
