package com.seveneow.simplechat.view_interface;

import java.util.List;


public interface BasicListMvpView extends BasicMvpView {
  int NOTIFY_ALL_DATA_CHANGED = 0;
  int NOTIFY_DATA_INSERT = 1;
  int NOTIFY_DATA_RANGE_CHANGED = 2;

  public boolean listIsAtBottom();

  public void scrollToBottom();

  public void setDataToList(List<Object> messages);

  public void notifyChanged(int type, Object... params);

  public int getItemCount();
}
