package com.seveneow.simplechat.utils;


public class RxEvent {
  public static final int EVENT_ROOM_MESSAGES_UPDATED = 0;
  public static final int EVENT_NOTIFICATION = 1;
  public static final int EVENT_DATA_UPDATE_NOTIFICATION = 2;
  public static final int EVENT_ROOM_MESSAGES_ADDED = 3;
  public static final int EVENT_ROOM_SINGLE_MESSAGES_UPDATED = 4;

  public int id;
  public Object[] params = {};
  public Object object;
}
