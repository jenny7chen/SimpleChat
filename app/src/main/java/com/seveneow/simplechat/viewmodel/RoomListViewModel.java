package com.seveneow.simplechat.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.seveneow.simplechat.model.Info;

public class RoomListViewModel extends BaseObservable {
  private Info info;
  private Context context;

  public RoomListViewModel(Info info, Context context) {
    this.info = info;
    this.context = context;
  }

  public String getName(){
    return info.getName();
  }

  public String getLatestMessageText(){
    return info.getLatestMessageShowText();
  }

  public String getAvatarUrl(){
    return info.getPhoto();
  }

  public int getVisibility(){
    return info.getType() == Info.TYPE_NONE ? View.GONE : View.VISIBLE;
  }

  public int getTextVisibility() {
    return info.getType() == Info.TYPE_BOARD ? View.GONE : View.VISIBLE;
  }

}
