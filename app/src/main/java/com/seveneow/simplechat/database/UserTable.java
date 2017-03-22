package com.seveneow.simplechat.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.seveneow.simplechat.model.User;

import net.sqlcipher.database.SQLiteDatabase;

public class UserTable {
  public static final String NAME = "Room";

  private static final String COLUMN_ID = "_id";
  private static final String COLUMN_USER_ID = "_USER_ID";
  private static final String COLUMN_USER_NAME = "_USER_NAME";
  private static final String COLUMN_USER_AVATAR = "_USER_AVATAR";

  private static final String CREATE_ROOM_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
      COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COLUMN_USER_ID + " TEXT NOT NULL DEFAULT '0', "
      + COLUMN_USER_AVATAR + " TEXT DEFAULT '', "
      + COLUMN_USER_NAME + " TEXT DEFAULT '',"
      + "UNIQUE(" + COLUMN_USER_ID + ") ON CONFLICT REPLACE)";

  public static void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_ROOM_TABLE);
  }

  public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + NAME);
    onCreate(db);
  }

  public static long insert(DBHelper sqlite, User user, String password) {
    ContentValues values = new ContentValues();
    values.put(COLUMN_USER_ID, user.getId());
    values.put(COLUMN_USER_NAME, user.getName());
    values.put(COLUMN_USER_AVATAR, user.getAvatar());
    return sqlite.getWritableDatabase(password).insert(NAME, null, values);
  }

  public static Object[] get(DBHelper dbHelper, String roomId, String password) {
    SQLiteDatabase db = dbHelper.getReadableDatabase(password);

    String[] cols = new String[]{COLUMN_USER_ID, COLUMN_ID, COLUMN_USER_NAME,
        COLUMN_USER_AVATAR};
    String[] selectionArgs = new String[]{roomId};
    Cursor cursor = db.query(NAME, cols, COLUMN_USER_ID + " = ?", selectionArgs, null, null, null);
    if (cursor.getCount() == 0) {
      return null;
    }

    Object[] data = new Object[cols.length];

    cursor.moveToFirst();
    data[0] = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
    data[1] = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
    data[2] = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
    data[3] = cursor.getString(cursor.getColumnIndex(COLUMN_USER_AVATAR));

    cursor.close();
    return data;
  }
}
