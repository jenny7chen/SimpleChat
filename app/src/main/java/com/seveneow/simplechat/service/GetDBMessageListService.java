package com.seveneow.simplechat.service;

import android.app.IntentService;
import android.content.Intent;

import com.seveneow.simplechat.database.DBHelper;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.MessageParser;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.RxEventSender;
import com.seveneow.simplechat.utils.Static;

import java.util.ArrayList;


public class GetDBMessageListService extends IntentService {
  public static final String PARAM_ROOM_ID = "room_id";
  public static final String PARAM_NOTIFY_INIT = "notify_init";

  public GetDBMessageListService() {
    super("GetDBMessageListService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    DebugLog.e("Baaa", "GetDBMessageListService start");
    String roomId = intent.getStringExtra(PARAM_ROOM_ID);
    boolean notifyInit = intent.getBooleanExtra(PARAM_NOTIFY_INIT, false);
    Room room = RoomManager.getInstance().getRoomById(roomId);
    if (room == null)
      return;

    //check if save to DB is in processing, if it's running, need to wait until data saving finished.
    while (Static.isMyServiceRunning(this, SaveMessageService.class)) {
      try {
        Thread.sleep(100);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    //get new message list from database
    DBHelper helper = DBHelper.getInstance(this);
    ArrayList<Message> messages = helper.getRoomMessages(new MessageParser(this), roomId, Static.DB_PASS, 100);
    DebugLog.e("Baaa", "local DB data size = " + messages.size());
    if (messages.size() > 0)
      RoomManager.getInstance().updateRoomMessages(roomId, messages);

    if (notifyInit) {
      RxEventSender.notifyRoomMessagesInited(roomId);
    }
    helper.close();
  }
}
