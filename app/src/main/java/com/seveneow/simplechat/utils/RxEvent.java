package com.seveneow.simplechat.utils;


public class RxEvent {
  public static final int EVENT_NOTIFICATION = 1;

  // send to chat presenter for refresh layout
  public static final int EVENT_ROOM_MESSAGE_LIST_UPDATED = 2;
  public static final int EVENT_ROOM_MESSAGE_ADDED = 3;
  public static final int EVENT_ROOM_MESSAGE_UPDATED = 4;
  public static final int EVENT_ROOM_MESSAGE_INIT = 5;

  //send to chat presenter for refresh data
  public static final int EVENT_ROOM_MESSAGE_IS_SAVED_TO_DB = 6;

  //send to room list presenter for refresh layout
  public static final int EVENT_ROOM_LIST_UPDATE = 7;
  public static final int EVENT_ROOM_LIST_INIT = 8;
  public static final int EVENT_ROOM_ADDED = 9;
  public static final int EVENT_ROOM_LATEST_MESSAGE_SHOW_TEXT_UPDATE = 10;

  public int id;
  public Object[] params = {};
  public Object object;
  public String roomId = "";
}
