package com.mercury.platform.ui.components.datatable.data;

import com.mercury.platform.ui.components.panel.misc.ViewDestroy;
import lombok.Getter;
import rx.subjects.ReplaySubject;

public abstract class MDataService<T> implements ViewDestroy {
    @Getter
    private ReplaySubject<Integer> totalValues = ReplaySubject.create();
    @Getter
    private ReplaySubject<T[]> values = ReplaySubject.create();

    private ReplaySubject<DataRequest> updateStream = ReplaySubject.create();
    private ReplaySubject<T> removeStream = ReplaySubject.create();

    private ReplaySubject<?> unsubscribe$ = ReplaySubject.create();

    public MDataService() {
        this.updateStream
                .takeUntil(this.unsubscribe$)
                .subscribe(dataRequest -> {
                    T[] data = this.getData(dataRequest);
                    this.values.onNext(data);
                    this.totalValues.onNext(data.length);
                });
        this.removeStream
                .takeUntil(this.unsubscribe$)
                .subscribe(this::removeData);
    }

    public void update(DataRequest dataRequest) {
        this.updateStream.onNext(dataRequest);
    }

    public void remove(T data) {
        this.removeStream.onNext(data);
    }

    public abstract T[] getData(DataRequest request);

    public abstract void removeData(T data);

    @Override
    public void onViewDestroy() {
        this.unsubscribe$.onNext(null);
        this.unsubscribe$.onCompleted();
    }
}
