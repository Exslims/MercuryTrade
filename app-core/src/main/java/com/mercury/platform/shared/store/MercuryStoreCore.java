package com.mercury.platform.shared.store;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.config.descriptor.SoundDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrVisibleState;
import com.mercury.platform.shared.entity.message.MercuryError;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.PlainMessageDescriptor;
import rx.subjects.PublishSubject;


public class MercuryStoreCore {
    public static final PublishSubject<SoundType> soundSubject = PublishSubject.create();
    public static final PublishSubject<SoundDescriptor> soundSettingsSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> dndSubject = PublishSubject.create();
    public static final PublishSubject<FrameVisibleState> frameVisibleSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> uiLoadedSubject = PublishSubject.create();
    public static final PublishSubject<MessageInterceptor> addInterceptorSubject = PublishSubject.create();
    public static final PublishSubject<MessageInterceptor> removeInterceptorSubject = PublishSubject.create();
    public static final PublishSubject<String> chatCommandSubject = PublishSubject.create();
    public static final PublishSubject<String> openChatSubject = PublishSubject.create();
    public static final PublishSubject<NotificationDescriptor> messageSubject = PublishSubject.create();
    public static final PublishSubject<NotificationDescriptor> outMessageSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> appLoadingSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> showingDelaySubject = PublishSubject.create();
    public static final PublishSubject<String> stringAlertSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> poeFolderChangedSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> checkOutPatchSubject = PublishSubject.create();
    public static final PublishSubject<Integer> chunkLoadedSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> buttonsChangedSubject = PublishSubject.create();
    public static final PublishSubject<String> tooltipSubject = PublishSubject.create();
    public static final PublishSubject<String> alertSubject = PublishSubject.create();
    public static final PublishSubject<String> playerJoinSubject = PublishSubject.create();
    public static final PublishSubject<String> playerLeftSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> requestPatchSubject = PublishSubject.create();
    public static final PublishSubject<String> showPatchNotesSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> shutdownAppSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> shutdownForUpdateSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> startUpdateSubject = PublishSubject.create();
    public static final PublishSubject<Integer> updateInfoSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> updateReadySubject = PublishSubject.create();
    public static final PublishSubject<Boolean> saveConfigSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> toDefaultSubject = PublishSubject.create();
    public static final PublishSubject<ProfileDescriptor> changeProfileSubject = PublishSubject.create();
    public static final PublishSubject<HotKeyDescriptor> hotKeySubject = PublishSubject.create();
    public static final PublishSubject<HotKeyDescriptor> hotKeyReleaseSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> blockHotkeySubject = PublishSubject.create();
    public static final PublishSubject<MercuryError> errorHandlerSubject = PublishSubject.create();
    public static final PublishSubject<AdrVisibleState> adrVisibleSubject = PublishSubject.create();
    public static final PublishSubject<Boolean> tradeWhisperSubject = PublishSubject.create();
    public static final PublishSubject<SoundDescriptor> soundDescriptorSubject = PublishSubject.create();

    public static final PublishSubject<NotificationDescriptor> newNotificationSubject = PublishSubject.create();
    public static final PublishSubject<NotificationDescriptor> removeNotificationSubject = PublishSubject.create();
    public static final PublishSubject<PlainMessageDescriptor> plainMessageSubject = PublishSubject.create();
    public static final PublishSubject<PlainMessageDescriptor> newScannerMessageSubject = PublishSubject.create();
    public static final PublishSubject<PlainMessageDescriptor> removeScannerNotificationSubject = PublishSubject.create();
    public static final PublishSubject<NotificationDescriptor> expiredNotificationSubject = PublishSubject.create();
}
