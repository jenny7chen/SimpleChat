package com.seveneow.simplechat.view_interface;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.seveneow.simplechat.message.Message;

import java.util.List;


public interface ChatMvpView extends MvpView {
  public void showLoading();

  public void showContent();

  public void showError();

  public void setData(List<Message> data, boolean isSingleMessage);

  public void loadData();

  public List<Message> getData();
}
