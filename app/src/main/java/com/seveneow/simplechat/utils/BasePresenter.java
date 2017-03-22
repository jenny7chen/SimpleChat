package com.seveneow.simplechat.utils;

import android.content.Intent;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.seveneow.simplechat.view_interface.BasicMvpView;


public abstract class BasePresenter<V extends BasicMvpView> extends MvpBasePresenter<V> {
  public abstract void onEvent(RxEvent event);

  public String getString(int id, String... parameter) {
    if (!isViewAttached())
      return "";
    return getView().getStringFromResource(id, parameter);
  }

  public void startService(Class serviceClass, Intent intent) {
    if (!isViewAttached())
      return;
    getView().startService(serviceClass, intent);
  }
}
