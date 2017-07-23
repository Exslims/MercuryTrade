package com.mercury.platform.ui.misc;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import com.mercury.platform.shared.entity.message.ItemMessage;
import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.ui.adr.routing.AdrComponentDefinition;
import com.mercury.platform.ui.adr.routing.AdrPageDefinition;
import com.mercury.platform.ui.components.panel.grid.ItemInfoPanel;
import com.mercury.platform.ui.components.panel.grid.TabInfoPanel;
import com.mercury.platform.ui.components.panel.message.MessagePanel;
import rx.subjects.PublishSubject;

import java.util.Map;

public class MercuryStoreUI {
    public static final PublishSubject<ItemMessage> closeGridItemSubject = PublishSubject.create();
    public static final PublishSubject<Message> closeMessage = PublishSubject.create();
    public static final PublishSubject<Boolean> collapseMessageSubject = PublishSubject.create();
    public static final PublishSubject<TabInfoPanel> dismissTabInfoPanelSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> expandMessageSubject = PublishSubject.create();
    public static final PublishSubject<ItemInfoPanel> itemCellStateSubject = PublishSubject.create();
    public static final PublishSubject<MessagePanel> reloadMessageSubject = PublishSubject.create();
    //Scale
    public static final PublishSubject<Map<String,Float>> saveScaleSubject = PublishSubject.create();
    public static final PublishSubject<Float> notificationScaleSubject = PublishSubject.create();
    public static final PublishSubject<Float> taskBarScaleSubject = PublishSubject.create();
    public static final PublishSubject<Float> itemPanelScaleSubject = PublishSubject.create();

    public static final PublishSubject<Boolean> scrollToEndSubject = PublishSubject.create();
    public static final PublishSubject<ItemMessage> showItemGridSubject = PublishSubject.create();
    public static final PublishSubject<Class<?>> packSubject = PublishSubject.create();
    public static final PublishSubject<Class<?>> repaintSubject = PublishSubject.create();

    //adr
    public static final PublishSubject<AdrPageDefinition> adrStateSubject = PublishSubject.create();
    public static final PublishSubject<AdrComponentDefinition> adrComponentStateSubject = PublishSubject.create();
    public static final PublishSubject<AdrProfileDescriptor> adrSelectProfileSubject = PublishSubject.create();
    public static final PublishSubject<AdrComponentDescriptor> adrReloadSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> adrRepaintSubject = PublishSubject.create();
    public static final PublishSubject<AdrComponentDescriptor> adrUpdateSubject = PublishSubject.create();
}
