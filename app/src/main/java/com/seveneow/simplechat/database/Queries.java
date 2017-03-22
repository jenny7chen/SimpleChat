package com.seveneow.simplechat.database;

class Queries {
  static final String DROP_USER_TABLE = "DROP TABLE " + UserTable.NAME + " IF EXISTS";
  static final String DROP_MESSAGE_TABLE = "DROP TABLE " + MessageTable.NAME + " IF EXISTS";
  static final String DROP_ROOM_TABLE = "DROP TABLE " + RoomTable.NAME + " IF EXISTS";
  static final String DROP_ROOM_USER_TABLE = "DROP TABLE " + RoomUserTable.NAME + " IF EXISTS";
}