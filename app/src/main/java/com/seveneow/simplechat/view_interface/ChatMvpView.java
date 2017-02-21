package com.seveneow.simplechat.view_interface;

import com.seveneow.simplechat.model.Message;

import java.util.List;


public interface ChatMvpView extends BasicMvpView {
  public void updateData(List<Message> data, boolean isSingleMessage);
  public void updatePendingData(List<Message> data, boolean isSingleMessage);
  public void showSnackBar(String message);
}
