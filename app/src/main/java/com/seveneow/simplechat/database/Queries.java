package com.seveneow.simplechat.database;

class Queries {
  //columns
  private static final String COLUMN_ID = "_id";
  private static final String COLUMN_NAME = "_NAME";
  private static final String COLUMN_AVATAR_URL = "_AVATAR_URL";

  //table user
  private static final String COLUMN_USER_ID = "_USER_ID";

  //message time
  private static final String COLUMN_MESSAGE_ID = "_MESSAGE_ID";
  private static final String COLUMN_MESSAGE_TIME = "_MESSAGE_TIME";
  private static final String COLUMN_MESSAGE_TEXT = "_MESSAGE_TEXT";


  static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS User" + "( " +
      COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      COLUMN_USER_ID + " VARCHAR(50), " +
      COLUMN_NAME + " VARCHAR(50), " +
      COLUMN_AVATAR_URL + " TEXT" +
      ");";
  static final String CREATE_MESSAGE_TABLE = "CREATE TABLE IF NOT EXISTS Message" + "( " +
      COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY, " +
      COLUMN_MESSAGE_TIME + " VARCHAR(50), " +
      COLUMN_MESSAGE_TEXT + " TEXT" +
      ");";
  static final String CREATE_ROOM_TABLE = "CREATE TABLE IF NOT EXISTS Room" + "( " +
      COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      COLUMN_NAME + " VARCHAR(50), " +
      COLUMN_AVATAR_URL + " TEXT" +
      ");";

  static final String DROP_USER_TABLE = "DROP TABLE User IF EXISTS";
  static final String DROP_MESSAGE_TABLE = "DROP TABLE Message IF EXISTS";
  static final String DROP_ROOM_TABLE = "DROP TABLE Room IF EXISTS";

}