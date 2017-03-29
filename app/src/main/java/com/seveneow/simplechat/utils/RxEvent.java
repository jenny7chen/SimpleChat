package com.seveneow.simplechat.utils;


public class RxEvent {
  public static final int EVENT_ROOM_MESSAGE_LIST_UPDATED = 1;
  public static final int EVENT_NOTIFICATION = 2;
  public static final int EVENT_DATA_UPDATE_NOTIFICATION = 3;
  public static final int EVENT_ROOM_MESSAGE_ADDED = 4;

  public static final int EVENT_ROOM_MESSAGE_UPDATED = 5;
  public static final int EVENT_ROOM_MESSAGE_INIT = 6;
  public static final int EVENT_ROOM_LIST_UPDATE = 7;
  public static final int EVENT_ROOM_LIST_INITED = 8;
  public static final int EVENT_ROOM_SHOWTEXT_UPDATE = 9;
  public static final int EVENT_ROOM_MESSAGE_SAVED = 10;
  public static final int EVENT_ROOM_INSERTED = 11;

  public int id;
  public Object[] params = {};
  public Object object;
}
