package com.seveneow.simplechat.database;

import android.content.ContentValues;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

public class RoomUserTable {
  public static final String NAME = "Room";

  private static final String COLUMN_ID = "_id";
  private static final String COLUMN_ROOM_ID = "_ROOM_ID";
  private static final String COLUMN_USER_ID = "_USER_ID";

  private static final String CREATE_ROOM_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
      COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COLUMN_ROOM_ID + " TEXT NOT NULL DEFAULT '0', "
      + COLUMN_USER_ID + " TEXT NOT NULL DEFAULT '0')";

  public static void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_ROOM_USER_TABLE);
  }

  public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + NAME);
    onCreate(db);
  }

  public static long insert(DBHelper sqlite, String roomId, String userId, String password) {
    ContentValues values = new ContentValues();
    values.put(COLUMN_ROOM_ID, roomId);
    values.put(COLUMN_USER_ID, userId);
    return sqlite.getWritableDatabase(password).insert(NAME, null, values);
  }

  public static ArrayList<String> get(DBHelper dbHelper, String roomId, String password) {
    SQLiteDatabase db = dbHelper.getReadableDatabase(password);

    String[] cols = new String[]{COLUMN_ID, COLUMN_ID, COLUMN_USER_ID};
    String[] selectionArgs = new String[]{roomId};
    Cursor cursor = db.query(NAME, cols, COLUMN_ROOM_ID + " = ?", selectionArgs, null, null, null);
    int rowCount = cursor.getCount();
    if (rowCount == 0) {
      return null;
    }
    cursor.moveToFirst();
    ArrayList<String> allUser = new ArrayList<>();
    for (int i = 0; i <= rowCount; i++) {
      if (cursor.moveToNext()) {
        allUser.add(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
      }
    }
    cursor.close();

    return allUser;
  }
}
