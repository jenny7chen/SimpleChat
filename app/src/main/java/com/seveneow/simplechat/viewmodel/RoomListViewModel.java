package com.seveneow.simplechat.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.seveneow.simplechat.model.Room;

public class RoomListViewModel extends BaseObservable {
  private Room room;
  private Context context;

  public RoomListViewModel(Room room, Context context) {
    this.room = room;
    this.context = context;
  }

  public String getName(){
    return room.getName();
  }

  public String getLatestMessageText(){
    return room.getLatestMessageShowText();
  }

  public String getAvatarUrl(){
    return room.getPhoto();
  }

  public int getVisibility(){
    return room.getType() == Room.TYPE_NONE ? View.GONE : View.VISIBLE;
  }

  public int getTextVisibility() {
    return room.getType() == Room.TYPE_BOARD ? View.GONE : View.VISIBLE;
  }

}
