package com.seveneow.simplechat.view_interface;

import android.content.Intent;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.seveneow.simplechat.model.Message;

import java.util.List;


public interface BasicMvpView extends MvpView {
  public void showLoading();

  public void showContent();

  public void showError();

  public void loadData();

  public List<Message> getData();

  public void startService(Class serviceClass, Intent intent);

  public String getStringFromResource(int id, String... parameter);

}
