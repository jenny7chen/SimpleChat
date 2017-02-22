package com.seveneow.simplechat.view_interface;

import com.seveneow.simplechat.model.Message;

import java.util.List;


public interface ChatMvpView extends BasicMvpView {
  public void updateData(List<Message> updatedData, boolean isSingleMessage, boolean isInsert);
  public void showSnackBar(String message);
}
