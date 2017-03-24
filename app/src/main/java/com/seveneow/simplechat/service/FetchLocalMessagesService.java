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


public class FetchLocalMessagesService extends IntentService {
  public static final String PARAM_ROOM_ID = "room_id";

  public FetchLocalMessagesService() {
    super("fetchLocalMessageService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    DebugLog.e("Baaa", "fetchLocalMessageService start");
    String roomId = intent.getStringExtra(PARAM_ROOM_ID);
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
    ArrayList<Message> messages = helper.getRoomMessages(new MessageParser(this), roomId, Static.DB_PASS);
    if (messages.size() > 0) {
      DebugLog.e("fetchLocalMessageService", "has db message return, message list size = " + messages.size());
      RoomManager.getInstance().updateRoomMessages(roomId, messages);
      RxEventSender.notifyRoomMessagesUpdated(roomId);
      return;
    }

    FDBManager.getServerMessages(roomId, this);
    helper.close();
  }
}
