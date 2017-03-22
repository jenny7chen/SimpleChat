package com.seveneow.simplechat.database;

import android.database.Cursor;

import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.TextMessage;
import com.seveneow.simplechat.utils.DebugLog;

import net.sqlcipher.DatabaseUtils;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

public class MessageTable {
  public static final String NAME = "Message";

  private static final String COLUMN_ID = "_id";
  private static final String COLUMN_MESSAGE_ROOM_ID = "_MESSAGE_ROOM_ID";
  private static final String COLUMN_MESSAGE_TYPE = "_MESSAGE_TYPE";
  private static final String COLUMN_MESSAGE_THUMBNAIL = "_MESSAGE_THUMBNAIL";
  private static final String COLUMN__MESSAGE_SHOW_SENDER = "_MESSAGE_SHOW_SENDER";
  public static final String COLUMN_MESSAGE_ID = "_MESSAGE_ID";
  public static final String COLUMN_MESSAGE_TIME = "_MESSAGE_TIME";
  public static final String COLUMN_MESSAGE_TEXT = "_MESSAGE_TEXT";
  public static final String COLUMN_MESSAGE_SENDER_ID = "_MESSAGE_SENDER_ID";

  // 14 days
  private static final long EXPIRY = 1209600000L;

  private static final String CREATE_MESSAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
      COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COLUMN_MESSAGE_ID + " TEXT NOT NULL DEFAULT '0', "
      + COLUMN_MESSAGE_SENDER_ID + " TEXT NOT NULL DEFAULT '0', "
      + COLUMN_MESSAGE_TIME + " TEXT DEFAULT '0', "
      + COLUMN_MESSAGE_TYPE + " INTEGER DEFAULT 0, "
      + COLUMN_MESSAGE_THUMBNAIL + " TEXT DEFAULT '', "
      + COLUMN_MESSAGE_TEXT + " TEXT DEFAULT '',"
      + COLUMN_MESSAGE_ROOM_ID + " TEXT DEFAULT '0',"
      + COLUMN__MESSAGE_SHOW_SENDER + " INTEGER DEFAULT 0,"
      + "UNIQUE(" + COLUMN_MESSAGE_ID + ") ON CONFLICT REPLACE)";

  public static void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_MESSAGE_TABLE);
  }

  public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + NAME);
    onCreate(db);
  }

  public static long insert(DBHelper sqlite, Message message, String password) {
    ArrayList<Message> messages = new ArrayList<>();
    messages.add(message);
    return insertMessages(sqlite, messages, password);
  }

  public static long insertMessages(DBHelper sqlite, ArrayList<Message> messages, String password) {
    return insertOrUpdateMessages(true, sqlite, messages, password);
  }

  public static long update(DBHelper sqlite, Message message, String password) {
    ArrayList<Message> messages = new ArrayList<>();
    messages.add(message);
    return updateMessages(sqlite, messages, password);
  }

  public static long updateMessages(DBHelper sqlite, ArrayList<Message> messages, String password) {
    return insertOrUpdateMessages(false, sqlite, messages, password);
  }

  private static long insertOrUpdateMessages(boolean insert, DBHelper sqlite, ArrayList<Message> messages, String password) {
    long result = -1;
    DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(sqlite.getWritableDatabase(password), NAME);
    // Get the numeric indexes for each of the columns that we're updating
    final int messageIdColumn = ih.getColumnIndex(COLUMN_MESSAGE_ID);
    final int messageSenderColumn = ih.getColumnIndex(COLUMN_MESSAGE_SENDER_ID);
    final int messageTimeColumn = ih.getColumnIndex(COLUMN_MESSAGE_TIME);
    final int messageTypeColumn = ih.getColumnIndex(COLUMN_MESSAGE_TYPE);
    final int messageRoomIdColumn = ih.getColumnIndex(COLUMN_MESSAGE_ROOM_ID);
    final int messageShowSenderColumn = ih.getColumnIndex(COLUMN__MESSAGE_SHOW_SENDER);
    final int messageTextColumn = ih.getColumnIndex(COLUMN_MESSAGE_TEXT);
    final int messageThumbnailColumn = ih.getColumnIndex(COLUMN_MESSAGE_THUMBNAIL);
    try {
      for (Message message : messages) {
        // Get the InsertHelper ready to insert a single row
        if (insert)
          ih.prepareForInsert();
        else
          ih.prepareForReplace();

        // Add the data for each column
        ih.bind(messageIdColumn, message.getId());
        ih.bind(messageSenderColumn, message.getSenderId());
        ih.bind(messageTimeColumn, message.getTime());
        ih.bind(messageTypeColumn, message.getType());
        ih.bind(messageRoomIdColumn, message.getRoomId());
        ih.bind(messageShowSenderColumn, message.isShowSender());

        if (message.getType() == Message.TYPE_TEXT) {
          ih.bind(messageTextColumn, ((TextMessage) message).getMessage());
        }
        else if (message.getType() == Message.TYPE_IMAGE) {
          ih.bind(messageThumbnailColumn, ((ImageMessage) message).getThumbnail());
        }

        // Insert the row into the database.
        result = ih.execute();
      }
    }
    finally {
      ih.close();
    }
    return result;
  }

  public static ArrayList<Object[]> getMessagesByRoomId(DBHelper dbHelper, String roomId, String password) {
    return getMessagesByArgs(dbHelper, new String[]{COLUMN_MESSAGE_ROOM_ID}, new String[]{roomId}, password);
  }

  public static ArrayList<Object[]> getMessagesByArgs(DBHelper dbHelper, String[] selectionCols, String[] args, String password) {
    SQLiteDatabase db = dbHelper.getReadableDatabase(password);

    String[] cols = new String[]{COLUMN_MESSAGE_ID, COLUMN_ID, COLUMN_MESSAGE_SENDER_ID,
        COLUMN_MESSAGE_TIME, COLUMN_MESSAGE_TYPE, COLUMN_MESSAGE_THUMBNAIL,
        COLUMN_MESSAGE_TEXT, COLUMN_MESSAGE_ROOM_ID, COLUMN__MESSAGE_SHOW_SENDER};

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < selectionCols.length; i++) {
      String col = selectionCols[i];
      sb.append(col);
      sb.append(" = ? ");
      if (i < selectionCols.length - 1) {
        sb.append("AND");
      }
    }
    ArrayList<Object[]> messages = new ArrayList<>();
    Cursor cursor = db.query(NAME, cols, sb.toString(), args, null, null, null);
    int rowCount = cursor.getCount();
    if (rowCount == 0) {
      cursor.close();
      return messages;
    }

    cursor.moveToFirst();
    for (int i = 0; i <= rowCount; i++) {
      if (cursor.moveToNext()) {
        Object[] data = new Object[cols.length];
        data[0] = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_ID));
        data[1] = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        data[2] = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_SENDER_ID));
        data[3] = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_TIME));
        data[4] = cursor.getLong(cursor.getColumnIndex(COLUMN_MESSAGE_TYPE));
        data[5] = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_THUMBNAIL));
        data[6] = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_TEXT));
        data[7] = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_ROOM_ID));
        data[8] = cursor.getLong(cursor.getColumnIndex(COLUMN__MESSAGE_SHOW_SENDER));

        if (data[0] == "0") {
          continue;
        }
        messages.add(data);
      }
    }
    cursor.close();
    DebugLog.e("baaa", "messages = " + messages);
    return messages;
  }

  public static Object[] get(DBHelper dbHelper, String messageId, String password) {
    SQLiteDatabase db = dbHelper.getReadableDatabase(password);

    String[] cols = new String[]{COLUMN_MESSAGE_ID, COLUMN_ID, COLUMN_MESSAGE_SENDER_ID,
        COLUMN_MESSAGE_TIME, COLUMN_MESSAGE_TYPE, COLUMN_MESSAGE_THUMBNAIL,
        COLUMN_MESSAGE_TEXT, COLUMN_MESSAGE_ROOM_ID, COLUMN__MESSAGE_SHOW_SENDER};
    String[] selectionArgs = new String[]{messageId};
    Cursor cursor = db.query(NAME, cols, COLUMN_MESSAGE_ID + " = ?", selectionArgs, null, null, null);
    if (cursor.getCount() == 0) {
      return null;
    }

    Object[] data = new Object[cols.length];

    cursor.moveToFirst();
    data[0] = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_ID));
    data[1] = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
    data[2] = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_SENDER_ID));
    data[3] = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_TIME));
    data[4] = cursor.getLong(cursor.getColumnIndex(COLUMN_MESSAGE_TYPE));
    data[5] = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_THUMBNAIL));
    data[6] = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_TEXT));
    data[7] = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_ROOM_ID));
    data[8] = cursor.getLong(cursor.getColumnIndex(COLUMN__MESSAGE_SHOW_SENDER));

    cursor.close();
    return data;
  }

  public static int cleanExpiredData(DBHelper sqlite, String password) {
    SQLiteDatabase db = sqlite.getWritableDatabase(password);

    long expire = new Date().getTime() - EXPIRY;
    String[] whereArgs = new String[]{String.valueOf(expire)};
    return db.delete(NAME, COLUMN_MESSAGE_TIME + " < ?", whereArgs);
  }
}
