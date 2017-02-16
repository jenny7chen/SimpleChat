package com.seveneow.simplechat.utils;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;


public abstract class BasePresenter<V extends MvpView> extends MvpBasePresenter<V> {
  public abstract void onEvent(RxEvent event);
}
