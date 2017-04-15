package com.mercury.platform.shared.store;

import lombok.Getter;
import rx.subjects.PublishSubject;


public class Reducer<T> {
    @Getter
    private String key;
    @Getter
    private PublishSubject<T> subject;
    protected Reducer(String key){
        this.key = key;
        this.subject = PublishSubject.create();
    }
}
