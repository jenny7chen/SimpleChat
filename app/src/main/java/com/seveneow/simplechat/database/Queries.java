package com.seveneow.simplechat.database;

class Queries {
  static final String DROP_USER_TABLE = "DROP TABLE " + HotTable.NAME + " IF EXISTS";
  static final String DROP_MESSAGE_TABLE = "DROP TABLE " + MessageTable.NAME + " IF EXISTS";
  static final String DROP_ROOM_TABLE = "DROP TABLE " + InfoTable.NAME + " IF EXISTS";
  static final String DROP_ROOM_USER_TABLE = "DROP TABLE " + InfoMemberTable.NAME + " IF EXISTS";
}