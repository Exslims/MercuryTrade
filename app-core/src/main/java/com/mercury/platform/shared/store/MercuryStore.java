package com.mercury.platform.shared.store;

import com.mercury.platform.core.misc.SoundType;
import rx.subjects.PublishSubject;

import java.util.Map;

public class MercuryStore {
    private static class MercuryStoreHolder {
        static final MercuryStore HOLDER_INSTANCE = new MercuryStore();
    }
    public static MercuryStore INSTANCE = MercuryStoreHolder.HOLDER_INSTANCE;

    public final PublishSubject<SoundType> soundSubject = PublishSubject.create();
    public final PublishSubject<Map<String,String>> soundSettingsSubject = PublishSubject.create();
}
