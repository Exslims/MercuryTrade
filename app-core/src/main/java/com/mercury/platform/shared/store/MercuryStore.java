package com.mercury.platform.shared.store;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.shared.FrameVisibleState;
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
}
