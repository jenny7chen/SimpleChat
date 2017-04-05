package com.seveneow.simplechat.database;

import android.content.ContentValues;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

public class InfoMemberTable {
  public static final String NAME = "RoomUser";

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
    return sqlite.openWritableDB(password).insert(NAME, null, values);
  }

  public static ArrayList<String> get(DBHelper helper, String roomId,String password){
    ArrayList<Object[]> resultList = getResultListByArgs(helper, new String[]{COLUMN_ROOM_ID}, new String[]{roomId}, password);
    ArrayList<String> userIdList = new ArrayList<>();
    for(Object[] data : resultList){
      userIdList.add((String)data[2]);
    }
    return userIdList;
  }

  public static ArrayList<Object[]> getResultListByArgs(DBHelper dbHelper, String[] selectionCols, String[] args, String password) {

    SQLiteDatabase db = dbHelper.openReadableDB(password);

    String[] cols = new String[]{COLUMN_ID, COLUMN_ROOM_ID, COLUMN_USER_ID};

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < selectionCols.length; i++) {
      String col = selectionCols[i];
      sb.append(col);
      sb.append(" = ? ");
      if (i < selectionCols.length - 1) {
        sb.append("AND");
      }
    }
    ArrayList<Object[]> resultList = new ArrayList<>();
    Cursor cursor = db.query(NAME, cols, sb.toString(), args, null, null, null);
    int rowCount = cursor.getCount();
    if (rowCount == 0) {
      cursor.close();
      return resultList;
    }
    cursor.moveToFirst();
    for (int i = 0; i < rowCount; i++) {
      Object[] data = new Object[cols.length];
      data[0] = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
      data[1] = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_ID));
      data[2] = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));

      if (data[1].equals("0")) {
        continue;
      }
      resultList.add(data);
      cursor.moveToNext();
    }
    cursor.close();
    return resultList;
  }
}
