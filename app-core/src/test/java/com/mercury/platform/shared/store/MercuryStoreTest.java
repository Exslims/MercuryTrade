package com.mercury.platform.shared.store;

import com.mercury.platform.core.misc.SoundType;
import org.junit.Test;


public class MercuryStoreTest {
    @Test
    public void testSoundReducer(){
        MercuryStore.INSTANCE.soundSubject
                .compose(DataTransformers.transformSoundData())
                .subscribe(System.out::println);

        MercuryStore.INSTANCE.soundSubject.onNext(SoundType.MESSAGE);

    }
}