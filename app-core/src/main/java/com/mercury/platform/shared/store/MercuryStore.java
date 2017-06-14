package com.mercury.platform.shared.store;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.entity.message.ItemMessage;
import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.shared.entity.SoundDescriptor;
import rx.subjects.PublishSubject;


public class MercuryStore {
    private static class MercuryStoreHolder {
        static final MercuryStore HOLDER_INSTANCE = new MercuryStore();
    }
    public static MercuryStore INSTANCE = MercuryStoreHolder.HOLDER_INSTANCE;

    public final PublishSubject<SoundType> soundSubject = PublishSubject.create();
    public final PublishSubject<SoundDescriptor> soundSettingsSubject = PublishSubject.create();
    public final PublishSubject<Boolean> dndSubject = PublishSubject.create();
    public final PublishSubject<FrameVisibleState> frameVisibleSubject = PublishSubject.create();
    public final PublishSubject<Boolean> uiLoadedSubject = PublishSubject.create();
    public final PublishSubject<MessageInterceptor> addInterceptorSubject = PublishSubject.create();
    public final PublishSubject<MessageInterceptor> removeInterceptorSubject = PublishSubject.create();
    public final PublishSubject<String> chatCommandSubject = PublishSubject.create();
    public final PublishSubject<String> openChatSubject = PublishSubject.create();
    public final PublishSubject<Message> messageSubject = PublishSubject.create();
    public final PublishSubject<Message> outMessageSubject = PublishSubject.create();
    public final PublishSubject<Boolean> appLoadingSubject = PublishSubject.create();
    public final PublishSubject<Boolean> showingDelaySubject = PublishSubject.create();
    public final PublishSubject<String> stringAlertSubject = PublishSubject.create();
    public final PublishSubject<Boolean> poeFolderChangedSubject = PublishSubject.create();
    public final PublishSubject<Boolean> checkOutPatchSubject = PublishSubject.create();
    public final PublishSubject<Integer> chunkLoadedSubject = PublishSubject.create();
    public final PublishSubject<Boolean> buttonsChangedSubject = PublishSubject.create();
    public final PublishSubject<String> tooltipSubject = PublishSubject.create();
    public final PublishSubject<String> notificationSubject = PublishSubject.create();
    public final PublishSubject<String> playerJoinSubject = PublishSubject.create();
    public final PublishSubject<String> playerLeftSubject = PublishSubject.create();
    public final PublishSubject<Boolean> requestPatchSubject = PublishSubject.create();
    public final PublishSubject<String> showPatchNotesSubject = PublishSubject.create();
    public final PublishSubject<Boolean> shutdownAppSubject = PublishSubject.create();
    public final PublishSubject<Boolean> shutdownForUpdateSubject = PublishSubject.create();
    public final PublishSubject<Boolean> startUpdateSubject = PublishSubject.create();
    public final PublishSubject<Integer> updateInfoSubject = PublishSubject.create();
    public final PublishSubject<Boolean> updateReadySubject = PublishSubject.create();

    //UI
    public final PublishSubject<ItemMessage> closeGridItemSubject = PublishSubject.create();
    public final PublishSubject<Message> closeMessagePanelSubject = PublishSubject.create();
    public final PublishSubject<Boolean> collapseMessagePanelSubject = PublishSubject.create();
    public final PublishSubject<Boolean> dismissTabInfoPanelSubject = PublishSubject.create();
}
