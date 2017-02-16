package com.seveneow.simplechat.utils;

import android.util.Log;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;

public class RxEventBus {
  private final static Relay<Object> bus = PublishRelay.create().toSerialized();

  public static void send(Object event) {
    Log.e("baaa", "dfdf" + ((RxEvent) event).object);
    bus.accept(event);
  }

  public static Observable<Object> toObservable() {
    return bus;
  }

  public static boolean hasObservers() {
    return bus.hasObservers();
  }
}
