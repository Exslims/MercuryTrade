package com.mercury.platform.ui.misc;


import com.mercury.platform.shared.entity.message.ItemMessage;
import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.ui.components.panel.grid.ItemInfoPanel;
import com.mercury.platform.ui.components.panel.grid.TabInfoPanel;
import com.mercury.platform.ui.components.panel.message.MessagePanel;
import rx.subjects.PublishSubject;

import java.util.Map;

public class MercuryStoreUI {
    private static class MercuryStoreHolder {
        static final MercuryStoreUI HOLDER_INSTANCE = new MercuryStoreUI();
    }
    public static MercuryStoreUI INSTANCE = MercuryStoreHolder.HOLDER_INSTANCE;

    public final PublishSubject<ItemMessage> closeGridItemSubject = PublishSubject.create();
    public final PublishSubject<Message> closeMessage = PublishSubject.create();
    public final PublishSubject<Boolean> collapseMessageSubject = PublishSubject.create();
    public final PublishSubject<TabInfoPanel> dismissTabInfoPanelSubject = PublishSubject.create();
    public final PublishSubject<Boolean> expandMessageSubject = PublishSubject.create();
    public final PublishSubject<ItemInfoPanel> itemCellStateSubject = PublishSubject.create();
    public final PublishSubject<MessagePanel> reloadMessageSubject = PublishSubject.create();
    //Scale
    public final PublishSubject<Map<String,Float>> saveScaleSubject = PublishSubject.create();
    public final PublishSubject<Float> notificationScaleSubject = PublishSubject.create();
    public final PublishSubject<Float> taskBarScaleSubject = PublishSubject.create();
    public final PublishSubject<Float> itemPanelScaleSubject = PublishSubject.create();

    public final PublishSubject<Boolean> scrollToEndSubject = PublishSubject.create();
    public final PublishSubject<ItemMessage> showItemGridSubject = PublishSubject.create();
    public final PublishSubject<Class<?>> packSubject = PublishSubject.create();
    public final PublishSubject<Class<?>> repaintSubject = PublishSubject.create();
}
