package com.seveneow.simplechat.database;

import android.database.Cursor;

import com.seveneow.simplechat.model.Room;

import net.sqlcipher.DatabaseUtils;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

public class FriendListTable {
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
    ArrayList<Room> updateList = new ArrayList<>();
    updateList.add(room);
    return insert(sqlite, updateList, password);
  }

  public static long insert(DBHelper sqlite, ArrayList<Room> rooms, String password) {
    return insertOrUpdateRooms(true, sqlite, rooms, password);
  }

  private static synchronized long insertOrUpdateRooms(boolean insert, DBHelper sqlite, ArrayList<Room> rooms, String password) {
    long result = -1;
    DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(sqlite.openWritableDB(password), NAME);
    // Get the numeric indexes for each of the columns that we're updating
    final int roomIdColumn = ih.getColumnIndex(COLUMN_ROOM_ID);
    final int roomNameColumn = ih.getColumnIndex(COLUMN_ROOM_NAME);
    final int roomLatestMessageTimeColumn = ih.getColumnIndex(COLUMN_LATEST_MESSAGE_TIME);
    final int roomLatestTextColumn = ih.getColumnIndex(COLUMN_LATEST_MESSAGE_TEXT);
    final int roomTypeColumn = ih.getColumnIndex(COLUMN_ROOM_TYPE);
    final int roomPhotoColumn = ih.getColumnIndex(COLUMN_ROOM_PHOTO);
    try {
      for (Room room : rooms) {
        if (insert)
          ih.prepareForInsert();
        else
          ih.prepareForReplace();

        // Add the data for each column
        ih.bind(roomIdColumn, room.getId());
        ih.bind(roomNameColumn, room.getName());
        ih.bind(roomLatestMessageTimeColumn, room.getLatestMessageTime());
        ih.bind(roomLatestTextColumn, room.getLatestMessageShowText());
        ih.bind(roomTypeColumn, room.getType());
        ih.bind(roomPhotoColumn, room.getPhoto());

        // Insert the row into the database.
        result = ih.execute();
      }
    }
    finally {
      ih.close();
    }
    return result;
  }

  public static Object[] get(DBHelper dbHelper, String roomId, String password) {
    SQLiteDatabase db = dbHelper.openReadableDB(password);

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
    data[2] = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_NAME));
    data[3] = cursor.getString(cursor.getColumnIndex(COLUMN_LATEST_MESSAGE_TIME));
    data[4] = cursor.getString(cursor.getColumnIndex(COLUMN_LATEST_MESSAGE_TEXT));
    data[5] = cursor.getLong(cursor.getColumnIndex(COLUMN_ROOM_TYPE));
    data[6] = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_PHOTO));

    cursor.close();
    return data;
  }

  public static ArrayList<Object[]> getRooms(DBHelper dbHelper, String password) {
    SQLiteDatabase db = dbHelper.openReadableDB(password);

    String[] cols = new String[]{COLUMN_ROOM_ID, COLUMN_ID, COLUMN_ROOM_NAME,
        COLUMN_LATEST_MESSAGE_TIME, COLUMN_LATEST_MESSAGE_TEXT, COLUMN_ROOM_TYPE, COLUMN_ROOM_PHOTO};

    ArrayList<Object[]> resultList = new ArrayList<>();
    Cursor cursor = db.query(NAME, cols, null, null, null, null, null);
    int rowCount = cursor.getCount();
    if (rowCount == 0) {
      cursor.close();
      return resultList;
    }
    cursor.moveToFirst();
    for (int i = 0; i < rowCount; i++) {
      Object[] data = new Object[cols.length];

      data[0] = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_ID));
      data[1] = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
      data[2] = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_NAME));
      data[3] = cursor.getString(cursor.getColumnIndex(COLUMN_LATEST_MESSAGE_TIME));
      data[4] = cursor.getString(cursor.getColumnIndex(COLUMN_LATEST_MESSAGE_TEXT));
      data[5] = cursor.getLong(cursor.getColumnIndex(COLUMN_ROOM_TYPE));
      data[6] = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_PHOTO));

      if (data[0].equals("0")) {
        continue;
      }
      resultList.add(data);
      cursor.moveToNext();
    }
    cursor.close();
    return resultList;
  }
}
