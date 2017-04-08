package com.mercury.platform.shared.store;

import com.alibaba.fastjson.JSON;
import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.shared.entity.FrameSettings;
import org.junit.Test;
import rx.observers.TestSubscriber;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MercuryStoreTest {
    @Test
    public void testSoundReducer(){
        TestSubscriber<Map<String,String>> testSubscriber = new TestSubscriber<>();

        MercuryStore.INSTANCE.soundSubject
                .compose(DataTransformers.transformSoundData())
                .subscribe(testSubscriber);

        MercuryStore.INSTANCE.soundSubject.onNext(SoundType.MESSAGE);
        Map<String,String> expectedValue = new HashMap<>();

//        Map<String,FrameSettings> settingss = new HashMap<>();
//        FrameSettings frameSettings = new FrameSettings(50, 50, 400, 400);
//        FrameSettings frameSettings1 = new FrameSettings(50, 50, 400, 400);
//        FrameSettings frameSettings2 = new FrameSettings(50, 50, 400, 400);
//
//        settingss.put("taskBar",frameSettings);
//        settingss.put("taskBar1",frameSettings1);
//        settingss.put("taskBar2",frameSettings2);
//        System.out.println(JSON.toJSONString(settingss));
    }
}