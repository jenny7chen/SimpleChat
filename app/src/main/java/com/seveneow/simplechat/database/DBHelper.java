package com.seveneow.simplechat.database;

import android.content.Context;

import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;
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
    RoomUserTable.onCreate(db);
    RoomTable.onCreate(db);
    UserTable.onCreate(db);
  }

  public void insertRoom(Room room, String password) {
    RoomTable.insert(this, room, password);
    for (String userId : room.getMembers()) {
      RoomUserTable.insert(this, room.getId(), userId, password);
    }
  }

  public void insertRooms(ArrayList<Room> rooms, String password) {
    RoomTable.insert(this, rooms, password);
    for (Room room : rooms) {
      for (String userId : room.getMembers()) {
        RoomUserTable.insert(this, room.getId(), userId, password);
      }
    }
  }

  public ArrayList<Room> getUserRoomList(RoomParser roomParser, String password) {
    ArrayList<Object[]> roomDataList = RoomTable.getRooms(this, password);
    ArrayList<Room> roomList = new ArrayList<>();
    for (Object[] roomData : roomDataList) {
      Room room = roomParser.parse(roomData);
      ArrayList<String> roomUserList = getRoomMembers(room.getId(), password);
      room.setMembers(roomUserList);
      roomList.add(room);
    }
    return roomList;
  }

  public ArrayList<String> getRoomMembers(String roomId, String password) {
    return RoomUserTable.getUserListByRoomId(this, roomId, password);

  }

  public void insertUser(User user, String password) {
    UserTable.insert(this, user, password);
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
