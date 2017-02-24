package com.seveneow.simplechat.model;

public class DBRequest {
  public static final int DB_INSERT = 0;
  public static final int DB_UPDATE = 1;
  public static final int DB_REMOVE = 2;

  public int type;
  public Object object;
}
