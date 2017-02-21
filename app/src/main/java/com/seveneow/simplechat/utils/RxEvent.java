package com.seveneow.simplechat.utils;


public class RxEvent {
  public static final int EVENT_ROOM_MESSAGES_UPDATED = 0;
  public static final int EVENT_NOTIFICATION = 1;
  public static final int EVENT_ROOM_MESSAGES_ADDED = 2;

  public int id;
  public Object[] params;
  public Object object;
}
