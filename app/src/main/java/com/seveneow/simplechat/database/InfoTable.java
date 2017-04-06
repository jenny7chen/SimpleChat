package com.seveneow.simplechat.database;

import android.database.Cursor;

import com.seveneow.simplechat.model.Info;
import com.seveneow.simplechat.utils.DebugLog;

import net.sqlcipher.DatabaseUtils;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

public class InfoTable {
  public static final String NAME = "Room";
  public static final int CHAT = 1;
  public static final int FAVORITE = 2;
  public static final int BOARD = 3;
  public static final int GROUP = 4;
  public static final int USER = 5;

  private static final String COLUMN_ID = "_id";
  private static final String COLUMN_INFO_ID = "_INFO_ID";
  private static final String COLUMN_LATEST_MESSAGE_TIME = "_LATEST_MESSAGE_TIME";
  private static final String COLUMN_LATEST_MESSAGE_TEXT = "_LATEST_MESSAGE_TEXT";
  private static final String COLUMN_INFO_NAME = "_NAME";
  private static final String COLUMN_INFO_TYPE = "_TYPE";
  private static final String COLUMN_ROOM_PHOTO = "_PHOTO";
  private static final String COLUMN_IS_FAVORITE = "_IS_FAVORITE";
  private static final String COLUMN_HAS_CHAT = "_HAS_CHAT";
  private static final String COLUMN_IS_PUBLIC = "_IS_PUBLIC";
  private static final String COLUMN_IS_BLOCK = "_IS_BLOCK";
  private static final String COLUMN_BOARD_URL = "_BOARD_URL";
  private static final String COLUMN_CREATOR = "_CREATOR";

  private static final String CREATE_ROOM_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
      COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COLUMN_INFO_ID + " TEXT NOT NULL DEFAULT '0', "
      + COLUMN_LATEST_MESSAGE_TIME + " TEXT DEFAULT '0', "
      + COLUMN_LATEST_MESSAGE_TEXT + " TEXT DEFAULT '', "
      + COLUMN_INFO_TYPE + " INTEGER DEFAULT 0, "
      + COLUMN_ROOM_PHOTO + " TEXT DEFAULT '', "
      + COLUMN_INFO_NAME + " TEXT DEFAULT '',"
      + COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0,"
      + COLUMN_HAS_CHAT + " INTEGER DEFAULT 0,"
      + COLUMN_IS_PUBLIC + " INTEGER DEFAULT 0,"
      + COLUMN_IS_BLOCK + " INTEGER DEFAULT 0,"
      + COLUMN_BOARD_URL + " TEXT DEFAULT '',"
      + COLUMN_CREATOR + " TEXT DEFAULT '',"
      + "UNIQUE(" + COLUMN_INFO_ID + ") ON CONFLICT REPLACE)";

  public static void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_ROOM_TABLE);
  }

  public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + NAME);
    onCreate(db);
  }

  public static long insert(int type, DBHelper sqlite, Info info, String password) {
    ArrayList<Info> updateList = new ArrayList<>();
    updateList.add(info);
    return insert(type, sqlite, updateList, password);
  }

  public static long insert(int type, DBHelper sqlite, ArrayList<Info> infos, String password) {
    return insertOrUpdateRooms(true, type, sqlite, infos, password);
  }

  private static synchronized long insertOrUpdateRooms(boolean insert, int type, DBHelper sqlite, ArrayList<Info> infos, String password) {
    long result = -1;
    DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(sqlite.openWritableDB(password), NAME);
    // Get the numeric indexes for each of the columns that we're updating
    final int infoIdColumn = ih.getColumnIndex(COLUMN_INFO_ID);
    final int roomNameColumn = ih.getColumnIndex(COLUMN_INFO_NAME);
    final int roomLatestMessageTimeColumn = ih.getColumnIndex(COLUMN_LATEST_MESSAGE_TIME);
    final int roomLatestTextColumn = ih.getColumnIndex(COLUMN_LATEST_MESSAGE_TEXT);
    final int roomTypeColumn = ih.getColumnIndex(COLUMN_INFO_TYPE);
    final int roomPhotoColumn = ih.getColumnIndex(COLUMN_ROOM_PHOTO);
    final int isFavoriteColumn = ih.getColumnIndex(COLUMN_IS_FAVORITE);
    final int hasChatColumn = ih.getColumnIndex(COLUMN_HAS_CHAT);
    final int isPublicColumn = ih.getColumnIndex(COLUMN_IS_PUBLIC);
    final int isBlockColumn = ih.getColumnIndex(COLUMN_IS_BLOCK);
    final int creatorColumn = ih.getColumnIndex(COLUMN_CREATOR);
    final int boardUrlColumn = ih.getColumnIndex(COLUMN_BOARD_URL);

    try {
      for (Info info : infos) {
        if (insert)
          ih.prepareForInsert();
        else
          ih.prepareForReplace();

        // Add the data for each column
        ih.bind(infoIdColumn, info.getId());
        ih.bind(roomNameColumn, info.getName());
        ih.bind(roomTypeColumn, info.getType());
        ih.bind(roomPhotoColumn, info.getPhoto());

        if (type == GROUP) {
          ih.bind(isFavoriteColumn, info.isFavorite());
          ih.bind(hasChatColumn, info.hasChat());
          ih.bind(isPublicColumn, info.isPublic());
          ih.bind(isBlockColumn, info.isBlock());
          ih.bind(creatorColumn, info.getCreatorId());
        }
        else if (type == BOARD) {
          ih.bind(isFavoriteColumn, info.isFavorite());
          ih.bind(boardUrlColumn, info.getBoardUrl());
        }
        else if (type == USER || type == FAVORITE) {
          ih.bind(isFavoriteColumn, info.isFavorite());
        }
        else if (type == CHAT) {
          ih.bind(roomLatestMessageTimeColumn, info.getLatestMessageTime());
          ih.bind(roomLatestTextColumn, info.getLatestMessageShowText());
          ih.bind(hasChatColumn, true);
        }
        else {
          ih.bind(isFavoriteColumn, info.isFavorite());
          ih.bind(hasChatColumn, info.hasChat());
          ih.bind(isPublicColumn, info.isPublic());
          ih.bind(isBlockColumn, info.isBlock());
          ih.bind(creatorColumn, info.getCreatorId());
          ih.bind(boardUrlColumn, info.getBoardUrl());
          ih.bind(roomLatestMessageTimeColumn, info.getLatestMessageTime());
          ih.bind(roomLatestTextColumn, info.getLatestMessageShowText());
        }

        // Insert the row into the database.
        result = ih.execute();
      }
    }catch (Exception e){
      DebugLog.printStackTrace(e);
    }
    return result;
  }

  public static Object[] get(DBHelper dbHelper, String infoId, String password) {
    SQLiteDatabase db = dbHelper.openReadableDB(password);

    String[] cols = new String[]{COLUMN_INFO_ID, COLUMN_ID, COLUMN_INFO_NAME,
        COLUMN_LATEST_MESSAGE_TIME, COLUMN_LATEST_MESSAGE_TEXT,
        COLUMN_INFO_TYPE, COLUMN_ROOM_PHOTO, COLUMN_IS_FAVORITE,
        COLUMN_HAS_CHAT, COLUMN_IS_PUBLIC, COLUMN_IS_BLOCK,
        COLUMN_CREATOR, COLUMN_BOARD_URL};

    String[] selectionArgs = new String[]{infoId};
    Cursor cursor = db.query(NAME, cols, COLUMN_INFO_ID + " = ?", selectionArgs, null, null, null);
    if (cursor.getCount() == 0) {
      return null;
    }

    Object[] data = new Object[cols.length];

    cursor.moveToFirst();
    data[0] = cursor.getString(cursor.getColumnIndex(COLUMN_INFO_ID));
    data[1] = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
    data[2] = cursor.getString(cursor.getColumnIndex(COLUMN_INFO_NAME));
    data[3] = cursor.getString(cursor.getColumnIndex(COLUMN_LATEST_MESSAGE_TIME));
    data[4] = cursor.getString(cursor.getColumnIndex(COLUMN_LATEST_MESSAGE_TEXT));
    data[5] = cursor.getLong(cursor.getColumnIndex(COLUMN_INFO_TYPE));
    data[6] = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_PHOTO));
    data[7] = cursor.getLong(cursor.getColumnIndex(COLUMN_IS_FAVORITE));
    data[8] = cursor.getLong(cursor.getColumnIndex(COLUMN_HAS_CHAT));
    data[9] = cursor.getLong(cursor.getColumnIndex(COLUMN_IS_PUBLIC));
    data[10] = cursor.getLong(cursor.getColumnIndex(COLUMN_IS_BLOCK));
    data[11] = cursor.getString(cursor.getColumnIndex(COLUMN_CREATOR));
    data[12] = cursor.getString(cursor.getColumnIndex(COLUMN_BOARD_URL));

    cursor.close();
    return data;
  }

  public static ArrayList<Object[]> getChats(DBHelper dbHelper, String password) {
    return getInfo(dbHelper, password, CHAT);
  }

  public static ArrayList<Object[]> getFavorites(DBHelper dbHelper, String password) {
    return getInfo(dbHelper, password, FAVORITE);
  }

  public static ArrayList<Object[]> getBoards(DBHelper dbHelper, String password) {
    return getInfo(dbHelper, password, BOARD);
  }

  public static ArrayList<Object[]> getGroups(DBHelper dbHelper, String password) {
    return getInfo(dbHelper, password, GROUP);
  }

  public static ArrayList<Object[]> getUsers(DBHelper dbHelper, String password) {
    return getInfo(dbHelper, password, USER);
  }

  public static ArrayList<Object[]> getInfo(DBHelper dbHelper, String password, int type) {
    SQLiteDatabase db = dbHelper.openReadableDB(password);

    String[] cols = new String[]{COLUMN_INFO_ID, COLUMN_ID, COLUMN_INFO_NAME,
        COLUMN_LATEST_MESSAGE_TIME, COLUMN_LATEST_MESSAGE_TEXT,
        COLUMN_INFO_TYPE, COLUMN_ROOM_PHOTO, COLUMN_IS_FAVORITE,
        COLUMN_HAS_CHAT, COLUMN_IS_PUBLIC, COLUMN_IS_BLOCK,
        COLUMN_CREATOR, COLUMN_BOARD_URL};

    ArrayList<Object[]> resultList = new ArrayList<>();

    String selectArgs = null;
    if (type == FAVORITE) {
      selectArgs = COLUMN_IS_FAVORITE + " = 1";
    }
    else if (type == CHAT) {
      selectArgs = COLUMN_HAS_CHAT + " = 1";
    }
    else if (type == BOARD) {
      selectArgs = COLUMN_INFO_TYPE + " = " + Info.TYPE_BOARD;
    }
    else if (type == GROUP) {
      selectArgs = COLUMN_INFO_TYPE + " = " + Info.TYPE_GROUP;
    }
    else if (type == USER) {
      selectArgs = COLUMN_INFO_TYPE + " = " + Info.TYPE_USER;
    }

    Cursor cursor = db.query(NAME, cols, selectArgs, null, null, null, null);

    int rowCount = cursor.getCount();
    if (rowCount == 0) {
      cursor.close();
      return resultList;
    }
    cursor.moveToFirst();
    for (int i = 0; i < rowCount; i++) {
      Object[] data = new Object[cols.length];

      data[0] = cursor.getString(cursor.getColumnIndex(COLUMN_INFO_ID));
      data[1] = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
      data[2] = cursor.getString(cursor.getColumnIndex(COLUMN_INFO_NAME));
      data[3] = cursor.getString(cursor.getColumnIndex(COLUMN_LATEST_MESSAGE_TIME));
      data[4] = cursor.getString(cursor.getColumnIndex(COLUMN_LATEST_MESSAGE_TEXT));
      data[5] = cursor.getLong(cursor.getColumnIndex(COLUMN_INFO_TYPE));
      data[6] = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_PHOTO));
      data[7] = cursor.getLong(cursor.getColumnIndex(COLUMN_IS_FAVORITE));
      data[8] = cursor.getLong(cursor.getColumnIndex(COLUMN_HAS_CHAT));
      data[9] = cursor.getLong(cursor.getColumnIndex(COLUMN_IS_PUBLIC));
      data[10] = cursor.getLong(cursor.getColumnIndex(COLUMN_IS_BLOCK));
      data[11] = cursor.getString(cursor.getColumnIndex(COLUMN_CREATOR));
      data[12] = cursor.getString(cursor.getColumnIndex(COLUMN_BOARD_URL));

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
