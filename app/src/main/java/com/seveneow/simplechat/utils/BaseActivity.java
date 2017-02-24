package com.seveneow.simplechat.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.seveneow.simplechat.view_interface.BasicListMvpView;
import com.seveneow.simplechat.view_interface.BasicMvpView;

import io.reactivex.android.schedulers.AndroidSchedulers;


public abstract class BaseActivity<V extends MvpView, P extends BasePresenter<V>>
    extends MvpViewStateActivity<V, P> implements BasicMvpView {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);  //when using viewstate needs to turn this on
    RxEventBus.toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(event -> presenter.onEvent((RxEvent) event));
  }

  public void startService(Class serviceClass, Intent intent) {
    intent.setClass(this, serviceClass);
    startService(intent);
  }

  public void startActivity(Class activityClass, Intent intent) {
    intent.setClass(this, activityClass);
    startActivity(intent);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    RxEventBus.toObservable().unsubscribeOn(AndroidSchedulers.mainThread());
  }

  public void hideSoftKeyboard() {
    View view = this.getCurrentFocus();
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }
}
