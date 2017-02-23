package com.seveneow.simplechat.view_interface;

public interface ChatListMvpView extends BasicListMvpView {

//  public void updateData(List<Message> updatedData, boolean isSingleMessage, boolean isInsert);
  public void showSnackBar(int messageId);
  public void setTitle(String titleText);
}
