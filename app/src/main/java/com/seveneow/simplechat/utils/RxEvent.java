package com.seveneow.simplechat.utils;


public class RxEvent {
  public static final int EVENT_ROOM_MESSAGES_UPDATED = 1;
  public static final int EVENT_NOTIFICATION = 2;
  public static final int EVENT_DATA_UPDATE_NOTIFICATION = 3;
  public static final int EVENT_ROOM_MESSAGES_ADDED = 4;

  public static final int EVENT_ROOM_SINGLE_MESSAGES_UPDATED = 5;
  public static final int EVENT_ROOM_MESSAGE_INIT = 6;
  public static final int EVENT_ROOM_LIST_UPDATE = 7;


  public int id;
  public Object[] params = {};
  public Object object;
}
