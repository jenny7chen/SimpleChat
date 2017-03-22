package com.seveneow.simplechat.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.seveneow.simplechat.model.Room;

import net.sqlcipher.database.SQLiteDatabase;

public class RoomTable {
  public static final String NAME = "Room";

  private static final String COLUMN_ID = "_id";
  private static final String COLUMN_ROOM_ID = "_ROOM_ID";
  private static final String COLUMN_LATEST_MESSAGE_TIME = "_LATEST_MESSAGE_TIME";
  private static final String COLUMN_LATEST_MESSAGE_TEXT = "_LATEST_MESSAGE_TEXT";
  private static final String COLUMN_ROOM_NAME = "_ROOM_NAME";
  private static final String COLUMN_ROOM_TYPE = "_ROOM_TYPE";
  private static final String COLUMN_ROOM_PHOTO = "_ROOM_PHOTO";

  private static final String CREATE_ROOM_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
      COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COLUMN_ROOM_ID + " TEXT NOT NULL DEFAULT '0', "
      + COLUMN_LATEST_MESSAGE_TIME + " TEXT DEFAULT '0', "
      + COLUMN_LATEST_MESSAGE_TEXT + " TEXT DEFAULT '', "
      + COLUMN_ROOM_TYPE + " INTEGER DEFAULT 0, "
      + COLUMN_ROOM_PHOTO + " TEXT DEFAULT '', "
      + COLUMN_ROOM_NAME + " TEXT DEFAULT '',"
      + "UNIQUE(" + COLUMN_ROOM_ID + ") ON CONFLICT REPLACE)";

  public static void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_ROOM_TABLE);
  }

  public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + NAME);
    onCreate(db);
  }

  public static long insert(DBHelper sqlite, Room room, String password) {
    ContentValues values = new ContentValues();
    values.put(COLUMN_ROOM_ID, room.getId());
    values.put(COLUMN_ROOM_NAME, room.getName());
    values.put(COLUMN_LATEST_MESSAGE_TIME, room.getLatestMessageTime());
    values.put(COLUMN_LATEST_MESSAGE_TEXT, room.getLatestMessageShowText());
    values.put(COLUMN_ROOM_TYPE, room.getType());
    values.put(COLUMN_ROOM_PHOTO, room.getPhoto());
    return sqlite.getWritableDatabase(password).insert(NAME, null, values);
  }

  public static Object[] get(DBHelper dbHelper, String roomId, String password) {
    SQLiteDatabase db = dbHelper.getReadableDatabase(password);

    String[] cols = new String[]{COLUMN_ROOM_ID, COLUMN_ID, COLUMN_ROOM_NAME,
        COLUMN_LATEST_MESSAGE_TIME, COLUMN_LATEST_MESSAGE_TEXT, COLUMN_ROOM_TYPE, COLUMN_ROOM_PHOTO};
    String[] selectionArgs = new String[]{roomId};
    Cursor cursor = db.query(NAME, cols, COLUMN_ROOM_ID + " = ?", selectionArgs, null, null, null);
    if (cursor.getCount() == 0) {
      return null;
    }

    Object[] data = new Object[cols.length];

    cursor.moveToFirst();
    data[0] = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_ID));
    data[1] = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
    data[1] = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_NAME));
    data[2] = cursor.getString(cursor.getColumnIndex(COLUMN_LATEST_MESSAGE_TIME));
    data[3] = cursor.getString(cursor.getColumnIndex(COLUMN_LATEST_MESSAGE_TEXT));
    data[4] = cursor.getLong(cursor.getColumnIndex(COLUMN_ROOM_TYPE));
    data[5] = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_PHOTO));

    cursor.close();
    return data;
  }
}
