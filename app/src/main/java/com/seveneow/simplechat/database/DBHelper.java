package com.seveneow.simplechat.database;

import android.content.Context;

import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Info;
import com.seveneow.simplechat.model.User;
import com.seveneow.simplechat.utils.BasePresenter;
import com.seveneow.simplechat.utils.MessageParser;
import com.seveneow.simplechat.utils.RoomParser;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {
  public final static String _DBName = "SampleList.db";
  private final static int _DBVersion = 1;
  private static DBHelper instance = null;
  private Context context;

  public static synchronized DBHelper getInstance(Context context) {
    if (instance == null) {
      instance = new DBHelper(context.getApplicationContext());
    }
    return instance;
  }

  public static void destroy() {
    if (instance != null) {
      instance.close();
      instance = null;
    }
  }

  private DBHelper(Context context) {
    super(context, _DBName, null, _DBVersion);
    this.context = context;
  }

  public SQLiteDatabase openWritableDB(String password) {
    SQLiteDatabase db = getWritableDatabase(password);
    if (!db.isOpen()) {
      db = SQLiteDatabase.openOrCreateDatabase(_DBName, password, null);
    }
    return db;
  }

  public SQLiteDatabase openReadableDB(String password) {
    SQLiteDatabase db = getWritableDatabase(password);
    if (!db.isOpen()) {
      db = SQLiteDatabase.openOrCreateDatabase(_DBName, password, null);
    }
    return db;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    initTable(db);
  }

  private void initTable(SQLiteDatabase db) {
    MessageTable.onCreate(db);
    InfoMemberTable.onCreate(db);
    InfoTable.onCreate(db);
    HotTable.onCreate(db);
  }

  public long insertRoom(Info info, String password) {
    long insertId =  InfoTable.insert(InfoTable.CHAT, this, info, password);
    for (String userId : info.getMembers()) {
      InfoMemberTable.insert(this, info.getId(), userId, password);
    }
    return insertId;
  }

  public void insertRooms(ArrayList<Info> infos, String password) {
    InfoTable.insert(InfoTable.CHAT, this, infos, password);
    for (Info info : infos) {
      for (String userId : info.getMembers()) {
        InfoMemberTable.insert(this, info.getId(), userId, password);
      }
    }
  }

  public ArrayList<Info> getUserRoomList(RoomParser roomParser, String password) {
    ArrayList<Object[]> roomDataList = InfoTable.getChats(this, password);
    ArrayList<Info> infoList = new ArrayList<>();
    for (Object[] roomData : roomDataList) {
      Info info = roomParser.parse(roomData);
      ArrayList<String> roomUserList = getRoomMembers(info.getId(), password);
      info.setMembers(roomUserList);
      infoList.add(info);
    }
    return infoList;
  }

  public ArrayList<String> getRoomMembers(String roomId, String password) {
    return InfoMemberTable.get(this, roomId, password);

  }

  public void insertUser(User user, String password) {
    HotTable.insert(this, user, password);
  }

  public long insertMessage(Message message, String password) {
    return MessageTable.insert(this, message, password);
  }

  public long insertMessageList(ArrayList<Message> messages, String password) {
    return MessageTable.insertMessages(this, messages, password);
  }

  public ArrayList<Message> getRoomMessages(MessageParser parser, String roomId, String password, int countLimit) {
    ArrayList<Message> messages = new ArrayList<>();
    ArrayList<Object[]> messageData = MessageTable.getMessagesByRoomId(this, roomId, password, countLimit);

    for (Object[] data : messageData) {
      Message message = parser.parse(data);
      if (message == null)
        continue;
      messages.add(message);
    }
    return messages;
  }

  public ArrayList<Message> searchMessage(BasePresenter presenter, String[] cols, String[] args, String password, int countLimit) {
    ArrayList<Message> messages = new ArrayList<>();
    MessageParser parser = new MessageParser(presenter);
    ArrayList<Object[]> messageData = MessageTable.getMessagesByArgs(this, cols, args, password, countLimit);
    for (Object[] data : messageData) {
      Message message = parser.parse(data);
      if (message == null)
        continue;
      messages.add(message);
    }
    return messages;
  }

  public static ArrayList<Message> parseDBObjectListToMessageList(ArrayList<Object[]> objectList, MessageParser parser) {
    ArrayList<Message> messages = new ArrayList<>();
    for (Object[] data : objectList) {
      Message message = parser.parse(data);
      if (message == null)
        continue;
      messages.add(message);
    }
    return messages;
  }

  public void updateMessage(Message message, String password) {
    MessageTable.update(this, message, password);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //TODO: onUpgrade what to do?
    db.execSQL(Queries.DROP_USER_TABLE);
    db.execSQL(Queries.DROP_MESSAGE_TABLE);
    db.execSQL(Queries.DROP_ROOM_TABLE);
    db.execSQL(Queries.DROP_ROOM_USER_TABLE);
  }
}
