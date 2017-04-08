package com.mercury.platform.shared.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;
import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.shared.entity.FrameSettings;
import com.mercury.platform.shared.entity.KeyData;
import com.mercury.platform.shared.entity.SoundDescriptor;
import org.junit.Test;
import rx.observers.TestSubscriber;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MercuryStoreTest {
    @Test
    public void testSoundReducer() throws IOException {
        TestSubscriber<Map<String,String>> testSubscriber = new TestSubscriber<>();

//        MercuryStore.INSTANCE.soundSubject
//                .compose(DataTransformers.transformSoundData())
//                .subscribe(testSubscriber);
//
//        MercuryStore.INSTANCE.soundSubject.onNext(SoundType.MESSAGE);
//        Map<String,String> expectedValue = new HashMap<>();

        JSONReader reader = new JSONReader(new FileReader("qwe"));
        reader.readObject();
        JSONWriter writer = new JSONWriter(new FileWriter("qwe"));
        List<KeyData<SoundDescriptor>> list = new ArrayList<>();
        list.add(new KeyData<>("1",new SoundDescriptor("test1.wav",10f)));
        list.add(new KeyData<>("2",new SoundDescriptor("test2.wav",10f)));
        list.add(new KeyData<>("3",new SoundDescriptor("test3.wav",10f)));
        System.out.println(JSON.toJSONString(new KeyData<>("frameSettings", list)));
    }
}