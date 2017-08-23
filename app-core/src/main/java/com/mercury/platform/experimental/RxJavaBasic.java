package com.mercury.platform.experimental;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by Константин on 07.04.2017.
 */
public class RxJavaBasic {
    public static void main(String[] args) {
        method5();
    }

    public static void method1() {
        Observable<String> observable = Observable.create(subscriber -> {
            subscriber.onNext("Hello World!");
            subscriber.onCompleted();
        });

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        };
        observable.subscribe(subscriber);
    }

    public static void method2() {
        Observable<String> observable = Observable.just("Hello world");
        Action1<String> onNextAction = System.out::println;
        observable.subscribe(onNextAction);
    }

    public static void method3() {
        Observable.just("Hello world").subscribe(System.out::println);
    }

    //Operators
    public static void method4() {
        Observable.just("Hello world").map(s -> s + " -Dan").subscribe(System.out::println);
    }

    public static void method5() {
        Observable.just("Hello world")
                .map(s -> s + " -Dan")
                .map(String::hashCode)
                .map(i -> Integer.toString(i))
                .subscribe(System.out::println);
    }

    public static void method6() {
        Observable.just("Hello world").subscribe(System.out::println);
    }
}
